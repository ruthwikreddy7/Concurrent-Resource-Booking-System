import http from 'k6/http';
import { check } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  vus: 1,
  iterations: 1,
  summaryTrendStats: ['avg', 'min', 'med', 'p(50)', 'p(90)', 'p(95)', 'p(99)', 'max'],
  thresholds: {
    http_req_failed: ['rate<0.05'],
    http_req_duration: ['p(95)<1000'],
  },
};

function jsonHeaders(extra = {}) {
  return {
    headers: {
      'Content-Type': 'application/json',
      ...extra,
    },
  };
}

function create(path, payload, extraHeaders = {}) {
  const response = http.post(`${BASE_URL}${path}`, JSON.stringify(payload), jsonHeaders(extraHeaders));
  check(response, {
    [`POST ${path} returns 2xx`]: (res) => res.status >= 200 && res.status < 300,
  });
  return response.json();
}

export default function () {
  const suffix = `${Date.now()}`;
  const theatre = create('/api/v1/theatres', {
    name: `SmokeTheatre${suffix}`,
    city: 'Hyderabad',
    address: 'Smoke test',
  });
  const screen = create(`/api/v1/theatres/${theatre.theatreId}/screens`, {
    name: `Screen${suffix}`,
    capacity: 4,
    seatsPerRow: 2,
  });
  const movie = create('/api/v1/movies', {
    title: `Smoke Movie ${suffix}`,
    durationMinutes: 120,
    language: 'English',
  });
  const user = create('/api/v1/users', {
    name: `SmokeUser${suffix}`,
    email: `smoke-${suffix}@example.com`,
  });
  const startTime = new Date(Date.now() + 60 * 60 * 1000).toISOString();
  const show = create('/api/v1/shows', {
    movieId: movie.movieId,
    screenId: screen.screenId,
    startTime,
  });
  const seatsResponse = http.get(`${BASE_URL}/api/v1/shows/${show.showId}/seats?page=0&size=4`);
  check(seatsResponse, {
    'show seats listed': (res) => res.status === 200 && res.json('content').length === 4,
  });
  const showSeatId = seatsResponse.json('content.0.showSeatId');
  const booking = create('/api/v1/bookings', {
    userId: user.userId,
    showId: show.showId,
    showSeatIds: [showSeatId],
  }, {
    'X-Idempotency-Key': `smoke-${suffix}`,
  });
  check(booking, {
    'booking held': (body) => body.status === 'HELD' && body.seats.length === 1,
  });
}
