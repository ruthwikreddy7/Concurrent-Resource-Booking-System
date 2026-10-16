
-- Flyway owns schema. Do not recreate these tables using Hibernate ddl-auto.

CREATE TABLE resources
(
    id           UUID PRIMARY KEY      DEFAULT uuidv7(),
    name         VARCHAR(120) NOT NULL,
    row_count    INTEGER      NOT NULL,
    column_count INTEGER      NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT chk_resource_name CHECK (
        length(btrim(name)) > 0 AND name = btrim(name)
        ),
    CONSTRAINT chk_resource_rows CHECK (row_count BETWEEN 1 AND 100),
    CONSTRAINT chk_resource_columns CHECK (column_count BETWEEN 1 AND 100),
    CONSTRAINT chk_resource_capacity CHECK (row_count * column_count <= 10000)
);

CREATE TABLE seats
(
    id          UUID PRIMARY KEY     DEFAULT uuidv7(),
    resource_id UUID        NOT NULL,
    row_no      INTEGER     NOT NULL,
    col_no      INTEGER     NOT NULL,
    status      VARCHAR(16) NOT NULL DEFAULT 'AVAILABLE',
    version     BIGINT      NOT NULL DEFAULT 0,
    CONSTRAINT fk_seat_resource FOREIGN KEY (resource_id)
        REFERENCES resources (id) ON DELETE RESTRICT,
    CONSTRAINT chk_seat_row CHECK (row_no BETWEEN 1 AND 100),
    CONSTRAINT chk_seat_column CHECK (col_no BETWEEN 1 AND 100),
    CONSTRAINT chk_seat_status CHECK (status IN ('AVAILABLE', 'BOOKED')),
    CONSTRAINT chk_seat_version CHECK (version >= 0),
    CONSTRAINT uq_seat_position UNIQUE (resource_id, row_no, col_no)
);

CREATE TABLE bookings
(
    id         UUID PRIMARY KEY     DEFAULT uuidv7(),
    seat_id    UUID        NOT NULL,
    user_id    UUID        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_booking_seat FOREIGN KEY (seat_id)
        REFERENCES seats (id) ON DELETE RESTRICT,
    CONSTRAINT uq_booking_seat UNIQUE (seat_id)
);

CREATE TABLE idempotency_records
(
    actor_id              UUID         NOT NULL,
    operation             VARCHAR(32)  NOT NULL,
    idempotency_key       VARCHAR(128) NOT NULL,
    request_sha256        BYTEA        NOT NULL,
    state                 VARCHAR(12)  NOT NULL,
    response_status       SMALLINT,
    response_body         TEXT,
    response_content_type VARCHAR(80),
    response_location     TEXT,
    created_at            TIMESTAMPTZ  NOT NULL DEFAULT now(),
    expires_at            TIMESTAMPTZ  NOT NULL DEFAULT (now() + INTERVAL '7 days'),
    CONSTRAINT pk_idempotency_record PRIMARY KEY (
                                                  actor_id, operation, idempotency_key
        ),
    CONSTRAINT chk_idempotency_operation CHECK (
        operation IN ('CREATE_RESOURCE', 'BOOK_SEAT')
        ),
    CONSTRAINT chk_idempotency_key CHECK (
        length(btrim(idempotency_key)) > 0
            AND length(idempotency_key) <= 128
        ),
    CONSTRAINT chk_request_hash CHECK (octet_length(request_sha256) = 32),
    CONSTRAINT chk_idempotency_state CHECK (state IN ('PENDING', 'COMPLETED')),
    CONSTRAINT chk_idempotency_outcome CHECK (
        (state = 'PENDING' AND response_status IS NULL
            AND response_body IS NULL AND response_content_type IS NULL
            AND response_location IS NULL)
            OR
        (state = 'COMPLETED' AND response_status BETWEEN 200 AND 599
            AND response_body IS NOT NULL AND response_content_type IS NOT NULL)
        ),
    CONSTRAINT chk_idempotency_expiry CHECK (expires_at > created_at)
);
