package com.carrental.availability.service;


import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanUpExpiredHold {
    private final JdbcTemplate jdbc;

    CleanUpExpiredHold(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Scheduled(fixedDelay = 30000) // every 30s
    @SchedulerLock(name = "purgeExpiredHolds", lockAtLeastFor = "PT5S", lockAtMostFor = "PT20S")
    void purgeExpired() {
        jdbc.update("DELETE FROM availability_slot WHERE status='HOLD' AND expires_at < now()");
    }
}
