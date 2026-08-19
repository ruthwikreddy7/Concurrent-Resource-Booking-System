# k6 Baseline Load Tests

These scripts establish the phase 0 measurement harness. They should expose the native baseline behavior without adding booking optimizations.

## Prerequisites

Start the app and database:

```bash
docker compose up -d
mvn spring-boot:run
```

Set a different API base URL if needed:

```bash
export BASE_URL=http://localhost:8080
```

## Scenarios

Smoke:

```bash
k6 run k6/scenarios/smoke.js
```

Normal booking baseline:

```bash
k6 run k6/scenarios/booking-baseline.js
```

Single-ShowSeat contention:

```bash
k6 run -e HOT_SEAT_VUS=100 k6/scenarios/hot-seat.js
k6 run -e HOT_SEAT_VUS=500 k6/scenarios/hot-seat.js
k6 run -e HOT_SEAT_VUS=1000 k6/scenarios/hot-seat.js
```

The hot-seat scenario creates one show, selects one `ShowSeat`, pre-creates one user per VU, and then drives every VU through exactly one measured `POST /api/v1/bookings` attempt for that same `ShowSeat`.

At the end of each run, the custom summary prints:

- `HTTP 201 successes`: booking attempts that returned HTTP 201.
- `successful ownership responses`: HTTP 201 responses whose body returned the contended `ShowSeat`.
- `seat-unavailable conflicts`: booking attempts that returned HTTP 409 with API error code `409000`.
- `duplicate allocations`: `successful ownership responses - allowed owners`, floored at zero.
- `duplicate-allocation rate`: duplicate allocations divided by total booking attempts.
- `p50`, `p95`, and `p99`: latency for the measured booking endpoint only.
- `throughput`: measured booking attempts per second from the first booking start to the last booking end, excluding setup.

The script prints `HOT_SEAT_ID=<show-seat-id>` during setup. After each run, verify the persisted owners independently:

```bash
HOT_SEAT_ID=<show-seat-id> k6/scripts/verify-hot-seat-db.sh
```
