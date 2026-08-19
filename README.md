# Concurrent Booking System

A scalable booking system designed to handle **multiple users competing for a limited number of resources at the same time**.

The project focuses on building a reusable booking platform that can be applied to different real-world scenarios such as movie seats, railway tickets, event tickets, hotel rooms, appointment slots, and other limited-resource reservations.

---

## 📌 Overview

Many real-world booking platforms face the same fundamental challenge:

> **A large number of users may try to reserve a limited number of resources simultaneously.**

For example:

- Multiple users trying to book the same movie seat
- Large numbers of users attempting railway ticket bookings
- Users competing for limited event tickets
- Customers trying to reserve the same hotel room
- Users trying to obtain limited appointment slots
- Users competing for limited darshan slots

Although these applications belong to different domains, the underlying engineering problem is largely the same:

```text
                    Many Users
                        |
                        v
              Concurrent Requests
                        |
                        v
               Limited Resources
                        |
                        v
              Resource Allocation
                        |
                        v
                Successful Booking
```

## Run

The app expects PostgreSQL on `localhost:5432` with database `concurrentbooking`
and credentials `postgres/postgres`. It uses Druid as the datasource implementation.

```bash
docker compose up -d
mvn clean install
mvn spring-boot:run
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## Booking Idempotency

`POST /api/v1/bookings` requires `X-Idempotency-Key`.

The request body contains only the booking data:

```json
{
  "userId": "user-id",
  "showId": "show-id",
  "showSeatIds": ["show-seat-id"]
}
```

Retrying the same request with the same `X-Idempotency-Key` returns the first stored create response. Reusing the same key with a different request returns a conflict.

## Database Migrations

Flyway owns schema evolution. Hibernate validates mappings after Flyway runs.

```text
src/main/resources/db/migration/V1__create_initial_schema.sql
```

The baseline migration contains the native schema. Future optimization phases should add new migrations instead of relying on Hibernate schema updates.

## Load Testing

k6 scenarios live under `k6/scenarios`:

```bash
k6 run k6/scenarios/smoke.js
k6 run k6/scenarios/booking-baseline.js
k6 run -e HOT_SEAT_VUS=100 k6/scenarios/hot-seat.js
```

The hot-seat scenario intentionally sends many clients to one show seat so the phase 0 concurrency behavior is measurable before any optimization work.
