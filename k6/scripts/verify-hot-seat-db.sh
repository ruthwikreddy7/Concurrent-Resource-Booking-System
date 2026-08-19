#!/usr/bin/env bash
set -euo pipefail

if [[ -z "${HOT_SEAT_ID:-}" ]]; then
  echo "HOT_SEAT_ID is required"
  echo "Example: HOT_SEAT_ID=<show-seat-id> k6/scripts/verify-hot-seat-db.sh"
  exit 1
fi

DB_CONTAINER="${DB_CONTAINER:-concurrentbooking-postgres}"
DB_NAME="${DB_NAME:-concurrentbooking}"
DB_USER="${DB_USER:-postgres}"

docker exec -i "$DB_CONTAINER" psql -U "$DB_USER" -d "$DB_NAME" \
  -v hot_seat_id="$HOT_SEAT_ID" <<'SQL'
SELECT
    bs.show_seat_id,
    COUNT(DISTINCT bs.booking_id) AS persisted_owner_count
FROM booking_seats bs
JOIN bookings b
    ON b.booking_id = bs.booking_id
WHERE bs.show_seat_id = :'hot_seat_id'
GROUP BY bs.show_seat_id;
SQL
