import http from 'k6/http';
import { check, sleep } from 'k6';
import exec from 'k6/execution';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  scenarios: {
    normal_booking: {
      executor: 'shared-iterations',
      vus: Number(__ENV.BASELINE_VUS || 10),
      iterations: Number(__ENV.BASELINE_ITERATIONS || 40),
      maxDuration: '2m',
    },
  },
  summaryTrendStats: ['avg', 'min', 'med', 'p(50)', 'p(90)', 'p(95)', 'p(99)', 'max'],
  thresholds: {
    http_req_failed: ['rate<0.20'],
    http_req_duration: ['p(50)<500', 'p(95)<1500', 'p(99)<2500'],
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
  return response.json();
}

export function setup() {
  const suffix = `${Date.now()}`;
  const theatre = create('/api/v1/theatres', {
    name: `BaseTheatre${suffix}`,
    city: 'Hyderabad',
    address: 'Booking baseline',
  });
  const screen = create(`/api/v1/theatres/${theatre.theatreId}/screens`, {
    name: `Screen${suffix}`,
    capacity: 80,
    seatsPerRow: 10,
  });
  const movie = create('/api/v1/movies', {
    title: `Baseline Movie ${suffix}`,
    durationMinutes: 120,
    language: 'English',
  });
  const startTime = new Date(Date.now() + 2 * 60 * 60 * 1000).toISOString();
  const show = create('/api/v1/shows', {
    movieId: movie.movieId,
    screenId: screen.screenId,
    startTime,
  });
  const seats = http.get(`${BASE_URL}/api/v1/shows/${show.showId}/seats?page=0&size=80`);
  check(seats, {
    'baseline seats available': (res) => res.status === 200 && res.json('content').length === 80,
  });
  return {
    suffix,
    showId: show.showId,
    showSeatIds: seats.json('content').map((seat) => seat.showSeatId),
  };
}

export default function (data) {
  const iteration = exec.scenario.iterationInTest;
  const showSeatId = data.showSeatIds[iteration % data.showSeatIds.length];
  const user = create('/api/v1/users', {
    name: `BaseUser${data.suffix}${iteration}`,
    email: `baseline-${data.suffix}-${iteration}@example.com`,
  });
  const response = http.post(`${BASE_URL}/api/v1/bookings`, JSON.stringify({
    userId: user.userId,
    showId: data.showId,
    showSeatIds: [showSeatId],
  }), requestParams({
    'X-Idempotency-Key': `baseline-${data.suffix}-${iteration}`,
  }));
  check(response, {
    'booking request completed': (res) => res.status === 201 || res.status === 409,
  });
  sleep(0.2);
}
