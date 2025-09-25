
-- Enable GiST support for btree columns once per DB (GiST = Generalized Search Tree)
CREATE EXTENSION IF NOT EXISTS btree_gist;

-- Core table: busy windows (BOOKED, HOLD, BLOCKED)
-- BIGSERIAL auto-increment BIGINT
CREATE TABLE IF NOT EXISTS availability_slot (
    id              BIGSERIAL       PRIMARY KEY,
    listing_id      TEXT          NOT NULL,
    period          tstzrange       NOT NULL,       -- half-open [start, end)
    status          TEXT            NOT NULL CHECK (status IN ('BOOKED', 'HOLD', 'BLOCKED')),
    booking_id      TEXT,
    expires_at      TIMESTAMPTZ      NOT NULL DEFAULT (now() + interval '3 minutes') -- only for HOLD
);

-- Janitor Speed: Handle 'HOLD' that expired (Janitor = the background task that deletes expired holds)
CREATE INDEX IF NOT EXISTS idx_availability_hold_expiry
    ON availability_slot (expires_at)  --ordered by expires_at
    WHERE status = 'HOLD';
