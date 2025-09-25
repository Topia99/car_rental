package com.carrental.availability.dao;

// jdbc: java database connectivity

import com.carrental.availability.dto.BusyWindow;
import com.carrental.availability.dto.HoldResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Repository
public class AvailabilityJdbc {
    private final JdbcTemplate jdbc;
    public AvailabilityJdbc(JdbcTemplate jdbc) {this.jdbc = jdbc;}

    // INSERT HOLD; let DB build the tstzrange; returns id & expires_at
    public HoldResponse insertHold(String listingId, Instant from, Instant to) {
        String sql = """
                INSERT INTO availability_slot(listing_id, period, status) 
                VALUES (?, tstzrange(?, ?, '[)'),'HOLD')
                RETURNING id, expires_at
                """;
        return jdbc.queryForObject(
                sql,
                (rs, _row) -> new HoldResponse(String.valueOf(rs.getLong("id")), rs.getTimestamp("expires_at").toInstant()),
                listingId, Timestamp.from(from), Timestamp.from(to));
    }

    // RELEASE (idempotent)
    public int releaseHold(Long holdId) {
        return jdbc.update("DELETE FROM availability_slot WHERE id = ? AND status = 'HOLD'", holdId);
    }

    // FIRM: HOLD -> BOOKED, then add cleanup BLOCKED right after booking end
    public void firmHold(Long holdId, String bookingId, Duration turnAroundTime) {
        // prompte to BOOKED and get listing_id + end instant
        var rec = jdbc.queryForObject("""
                Update availability_slot
                SET status='BOOKED', booking_id=?
                WHERE id=? AND status='HOLD' AND expires_at > now()
                RETURNING listing_id, upper(period) AS end_at
                """,
                (rs, _row) -> new Object[]{ rs.getString("listing_id"), rs.getTimestamp("end_at").toInstant()},
                bookingId, holdId);
        if(rec == null) throw new IllegalStateException("hold_expired_or_missing");

        String listingId = (String) rec[0];
        Instant endAt = (Instant) rec[1];
        Instant turnAroundTimeEndAt = endAt.plus(turnAroundTime);

        // insert cleanup block [end, end+cleanup)
        jdbc.update("""
                INSERT INTO availability_slot(listing_id, period, status)
                VALUES (
                    ?,
                    tstzrange(?, ?, '[)'),
                    'BLOCKED'
                )
                """,
                listingId, Timestamp.from(endAt), Timestamp.from(turnAroundTimeEndAt));
    }

    // Busy windows for UI shading (BOOKED + unexpired HOLD + BLOCKED)
    public List<BusyWindow> findBusy(String listingId, Instant from, Instant to) {
        return jdbc.query("""
                SELECT lower(period) AS from_utc, upper(period) AS to_utc, status
                FROM availability_slot
                WHERE listing_id = ?
                AND status <> 'HOLD'
                AND period && tstzrange(?, ?, '[)')
                ORDER BY from_utc
                """,
                (rs, _row) -> new BusyWindow(
                        rs.getTimestamp("from_utc").toInstant(),
                        rs.getTimestamp("to_utc").toInstant(),
                        rs.getString("status")),
                listingId, Timestamp.from(from), Timestamp.from(to));
    }

    // Boolean check with prep buffers (before/after)
    public boolean isAvailable(String listingId, Instant from, Instant to) {
        Boolean ok = jdbc.queryForObject("""
                SELECT NOT EXISTS (
                    SELECT 1
                    FROM availability_slot s
                    WHERE s.listing_id = ?
                    AND (s.status <> 'HOLD' OR s.expires_at > now())
                    AND s.period && tstzrange(?, ?, '[)')
                ) AS ok
                """,
                Boolean.class,
                listingId, Timestamp.from(from), Timestamp.from(to));

        return Boolean.TRUE.equals(ok);
    }


}
