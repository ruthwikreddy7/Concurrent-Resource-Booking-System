import http from 'k6/http';
import { check } from 'k6';
import exec from 'k6/execution';
import { Counter, Trend } from 'k6/metrics';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const HOT_SEAT_VUS = Number(__ENV.HOT_SEAT_VUS || 100);
const SETUP_TIMEOUT = __ENV.SETUP_TIMEOUT || '5m';
const SEAT_NOT_AVAILABLE_CODE = 409000;
const ALLOWED_OWNERS = 1;

const bookingAttempts = new Counter('booking_attempts');
const httpCreatedResponses = new Counter('http_created_responses');
const successfulOwnershipResponses = new Counter('successful_ownership_responses');
const seatUnavailableConflicts = new Counter('seat_unavailable_conflicts');
const bookingUnexpectedFailures = new Counter('booking_unexpected_failures');
const bookingAttemptDuration = new Trend('booking_attempt_duration', true);
const bookingStartedAt = new Trend('booking_started_at');
const bookingEndedAt = new Trend('booking_ended_at');

export const options = {
  setupTimeout: SETUP_TIMEOUT,
  scenarios: {
    hot_seat: {
      executor: 'per-vu-iterations',
      vus: HOT_SEAT_VUS,
      iterations: 1,
      maxDuration: '3m',
    },
  },
  summaryTrendStats: ['avg', 'min', 'med', 'p(50)', 'p(90)', 'p(95)', 'p(99)', 'max'],
  thresholds: {
    booking_attempts: [`count==${HOT_SEAT_VUS}`],
    booking_unexpected_failures: ['count==0'],
  },
};

function requestParams(extra = {}) {
  return {
    headers: {
      'Content-Type': 'application/json',
      ...extra,
    },
  };
}

function create(path, payload, extraHeaders = {}) {
  const response = http.post(`${BASE_URL}${path}`, JSON.stringify(payload), requestParams(extraHeaders));
  check(response, {
    [`POST ${path} returns 2xx`]: (res) => res.status >= 200 && res.status < 300,
  });
  if (response.status < 200 || response.status >= 300) {
    throw new Error(`POST ${path} failed with HTTP ${response.status}: ${response.body}`);
  }
  return response.json();
}

export function setup() {
  const suffix = `${Date.now()}`;
  const theatre = create('/api/v1/theatres', {
    name: `HotTheatre${suffix}`,
    city: 'Hyderabad',
    address: 'Hot-seat contention',
  });
  const screen = create(`/api/v1/theatres/${theatre.theatreId}/screens`, {
    name: `Screen${suffix}`,
    capacity: 24,
    seatsPerRow: 12,
  });
  const movie = create('/api/v1/movies', {
    title: `Hot Seat Movie ${suffix}`,
    durationMinutes: 120,
    language: 'English',
  });
  const startTime = new Date(Date.now() + 3 * 60 * 60 * 1000).toISOString();
  const show = create('/api/v1/shows', {
    movieId: movie.movieId,
    screenId: screen.screenId,
    startTime,
  });
  const seats = http.get(`${BASE_URL}/api/v1/shows/${show.showId}/seats?page=0&size=24&sortBy=createdAt&sortOrder=asc`);
  check(seats, {
    'hot-seat inventory loaded': (res) => res.status === 200 && res.json('content').length > 0,
  });
  if (seats.status !== 200 || seats.json('content').length === 0) {
    throw new Error(`Hot-seat inventory was not created for show ${show.showId}`);
  }

  const userIds = [];
  for (let i = 0; i < HOT_SEAT_VUS; i += 1) {
    const user = create('/api/v1/users', {
      name: `HotUser${suffix}${i}`,
      email: `hot-seat-${suffix}-${i}@example.com`,
    });
    userIds.push(user.userId);
  }

  const result = {
    suffix,
    showId: show.showId,
    hotSeatId: seats.json('content.0.showSeatId'),
    userIds,
  };
  console.log(`HOT_SEAT_ID=${result.hotSeatId}`);
  console.log(`DB verification: HOT_SEAT_ID=${result.hotSeatId} k6/scripts/verify-hot-seat-db.sh`);
  return result;
}

export default function (data) {
  const userIndex = exec.vu.idInTest - 1;
  bookingStartedAt.add(Date.now());
  const response = http.post(`${BASE_URL}/api/v1/bookings`, JSON.stringify({
    userId: data.userIds[userIndex],
    showId: data.showId,
    showSeatIds: [data.hotSeatId],
  }), requestParams({
    'X-Idempotency-Key': `hot-seat-${data.suffix}-${userIndex}`,
  }));
  bookingEndedAt.add(Date.now());
  bookingAttempts.add(1);
  bookingAttemptDuration.add(response.timings.duration);

  const isCreated = response.status === 201;
  const body = parseJson(response);
  const isSeatUnavailableConflict = response.status === 409
    && body
    && body.code === SEAT_NOT_AVAILABLE_CODE;
  const ownsHotSeat = isCreated
    && body
    && body.userId === data.userIds[userIndex]
    && Array.isArray(body.seats)
    && body.seats.some((seat) => seat.showSeatId === data.hotSeatId);

  if (isCreated) {
    httpCreatedResponses.add(1);
  }
  if (ownsHotSeat) {
    successfulOwnershipResponses.add(1);
  } else if (isSeatUnavailableConflict) {
    seatUnavailableConflicts.add(1);
  } else {
    bookingUnexpectedFailures.add(1);
  }

  check(response, {
    'hot-seat attempt returns 201 or seat-unavailable 409': () => isCreated || isSeatUnavailableConflict,
    '201 response owns requested ShowSeat': () => !isCreated || ownsHotSeat,
  });
}

function parseJson(response) {
  try {
    return response.json();
  } catch (error) {
    return null;
  }
}

function countMetric(data, name) {
  return data.metrics[name] && data.metrics[name].values.count
    ? data.metrics[name].values.count
    : 0;
}

function valueMetric(data, name, valueName) {
  return data.metrics[name] && data.metrics[name].values[valueName] !== undefined
    ? data.metrics[name].values[valueName]
    : 0;
}

function fixed(value, digits = 2) {
  return Number(value || 0).toFixed(digits);
}

function percent(value) {
  return fixed(value * 100);
}

export function handleSummary(data) {
  const attempts = countMetric(data, 'booking_attempts');
  const createdResponses = countMetric(data, 'http_created_responses');
  const ownershipResponses = countMetric(data, 'successful_ownership_responses');
  const conflicts = countMetric(data, 'seat_unavailable_conflicts');
  const unexpected = countMetric(data, 'booking_unexpected_failures');
  const duplicateAllocations = Math.max(0, ownershipResponses - ALLOWED_OWNERS);
  const duplicateAllocationRate = attempts > 0 ? duplicateAllocations / attempts : 0;
  const bookingWindowMs = Math.max(1,
    valueMetric(data, 'booking_ended_at', 'max') - valueMetric(data, 'booking_started_at', 'min'));
  const throughput = (attempts / bookingWindowMs) * 1000;
  const p50 = valueMetric(data, 'booking_attempt_duration', 'p(50)');
  const p95 = valueMetric(data, 'booking_attempt_duration', 'p(95)');
  const p99 = valueMetric(data, 'booking_attempt_duration', 'p(99)');

  const summary = {
    scenario: 'hot-seat single ShowSeat contention',
    concurrentUsers: HOT_SEAT_VUS,
    showSeatsUnderContention: 1,
    bookingAttempts: attempts,
    http201Successes: createdResponses,
    successfulOwnershipResponses: ownershipResponses,
    allowedOwners: ALLOWED_OWNERS,
    seatUnavailableConflicts: conflicts,
    duplicateAllocations,
    duplicateAllocationRate,
    unexpectedFailures: unexpected,
    bookingLatencyMs: {
      p50,
      p95,
      p99,
    },
    throughput: {
      bookingAttemptsPerSecond: throughput,
      measuredBookingWindowMs: bookingWindowMs,
    },
  };

  return {
    stdout: [
      '',
      'Hot-seat single ShowSeat contention summary',
      `concurrent users: ${HOT_SEAT_VUS}`,
      'show seats under contention: 1',
      `booking attempts: ${attempts}`,
      `HTTP 201 successes: ${createdResponses}`,
      `successful ownership responses: ${ownershipResponses}`,
      `allowed owners: ${ALLOWED_OWNERS}`,
      `seat-unavailable conflicts: ${conflicts}`,
      `duplicate allocations: ${duplicateAllocations}`,
      `duplicate-allocation rate: ${percent(duplicateAllocationRate)}%`,
      `unexpected failures: ${unexpected}`,
      `p50 booking latency: ${fixed(p50)} ms`,
      `p95 booking latency: ${fixed(p95)} ms`,
      `p99 booking latency: ${fixed(p99)} ms`,
      `throughput: ${fixed(throughput)} booking attempts/s`,
      `measured booking window: ${fixed(bookingWindowMs)} ms`,
      '',
      JSON.stringify(summary, null, 2),
      '',
    ].join('\n'),
  };
}
