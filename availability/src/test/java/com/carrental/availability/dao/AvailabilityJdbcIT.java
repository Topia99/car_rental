package com.carrental.availability.dao;

import com.carrental.availability.dto.BusyWindow;
import com.carrental.availability.service.CleanUpExpiredHold;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@SpringBootTest(
        classes = {AvailabilityJdbc.class},
        properties = {
                "spring.flyway.enabled=true",
                "spring.jpa.hibernate.ddl-auto=validate",
                "spring.flyway.locations=classpath:db/migration",
                "logging.level.org.flywaydb=DEBUG"
        }
)
@ImportAutoConfiguration({ DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class, FlywayAutoConfiguration.class})
public class AvailabilityJdbcIT {

    @Container
    static final PostgreSQLContainer<?> pg =
            new PostgreSQLContainer<>("postgres:16.0")
                    .withDatabaseName("carrental")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void dbProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", pg::getJdbcUrl);
        r.add("spring.datasource.username", pg::getUsername);
        r.add("spring.datasource.password", pg::getPassword);
    }

    @Autowired
    AvailabilityJdbc jdbc;


    @Test
    void excludeConstraint_blocksOverlap_allowsTouching() {
        String L = "L1";
        Instant a1 = Instant.parse("2025-01-01T10:00:00Z");
        Instant a2 = Instant.parse("2025-01-01T12:00:00Z");
        // first hold ok
        var h1 = jdbc.insertHold(L, a1, a2);

        // exact overlap -> should throw DataAccessException (constraint)
        assertThatThrownBy(() -> jdbc.insertHold(
                L,
                Instant.parse("2025-01-01T11:00:00Z"), //overlap
                Instant.parse("2025-01-01T13:00:00Z")
        )).isInstanceOf(org.springframework.dao.DataAccessException.class);

        // touching at boundary is allowed([10,12) and [12,14))
        var h2 = jdbc.insertHold(
                L,
                Instant.parse("2025-01-01T12:00:00Z"),
                Instant.parse("2025-01-01T14:00:00Z")
        );
        assertThat(h2).isNotNull();
        assertThat(h1).isNotNull();
    }


    @Test
    void reads_ignoreExpireHolds_and_firmAddsBlockedCleanup() {
        String L = "L2";
        Instant from = Instant.parse("2025-02-01T08:00:00Z");
        Instant to = Instant.parse("2025-02-01T10:00:00Z");

        // 1) expired hold (negative TTl) should NOT appear as busy
        jdbc.insertHold(L, from, to);

        // The BusyWindow will not return 'HOLD' windows
        List<BusyWindow> busy1 = jdbc.findBusy(
                L,
                Instant.parse("2025-02-01T00:00:00Z"),
                Instant.parse("2025-02-02T00:00:00Z"));
        assertThat(busy1).isEmpty();

        from = Instant.parse("2025-02-03T08:00:00Z");
        to = Instant.parse("2025-02-03T10:00:00Z");

        // 2) real hold -> firm to booked + blocked (30m)
        var hold = jdbc.insertHold(L, from, to);
        jdbc.firmHold(Long.parseLong(hold.id()), "B100", Duration.ofMinutes(30));

        List<BusyWindow> busy2 = jdbc.findBusy(
                L,
                Instant.parse("2025-02-03T00:00:00Z"),
                Instant.parse("2025-02-04T00:00:00Z"));

        assertThat(busy2).hasSize(2); // Booked + Blocked
        // assert booked window
        assertThat(busy2.get(0).from()).isEqualTo(from);
        assertThat(busy2.get(0).to()).isEqualTo(to);

        // assert block window
        assertThat(busy2.get(1).from()).isEqualTo(to);
        assertThat(busy2.get(1).to()).isEqualTo(to.plus(Duration.ofMinutes(30)));
    }

    @Test
    void isAvailable_respectsPrepBuffers() {
        String L = "L3";
        Instant a1 = Instant.parse("2025-03-01T10:00:00Z");
        Instant a2 = Instant.parse("2025-03-01T12:00:00Z");

        jdbc.insertHold(L, a1, a2);

        // Request [11:00, 14:00) is available, should return False;
        boolean ok1 = jdbc.isAvailable(L,
                Instant.parse("2025-03-01T11:00:00Z"),
                Instant.parse("2025-03-01T14:00:00Z"));
        assertThat(ok1).isFalse();

        // Request [12:00, 14:00) is available with no buffer
        boolean ok2 = jdbc.isAvailable(L, Instant.parse("2025-03-01T12:00:00Z"),
                Instant.parse("2025-03-01T14:00:00Z"));
        assertThat(ok2).isTrue();


    }
}
