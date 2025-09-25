

-- Prevent overlaps per listing for busy kinds (atomic; DB-enforced)
ALTER TABLE availability_slot
    ADD CONSTRAINT availability_no_overlaps
    EXCLUDE USING gist (
        listing_id WITH =,
        period WITH &&
    )
    WHERE (status IN ('BOOKED', 'HOLD', 'BLOCKED'));