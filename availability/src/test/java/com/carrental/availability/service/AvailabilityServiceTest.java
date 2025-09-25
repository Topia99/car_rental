package com.carrental.availability.service;

import com.carrental.availability.dao.AvailabilityJdbc;
import com.carrental.availability.dto.HoldRequest;
import com.carrental.availability.dto.HoldResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceTest {
    @Mock AvailabilityJdbc jdbc;
    @InjectMocks HoldService svc;

    @Test
    /*
    Test if the service correctly maps the request through to the repository
    and returns the repository’s response.
     */
    void createHold_success_mapsThrough() {
        HoldRequest req = new HoldRequest("L1", "2025-01-01T10:00:00Z", "2025-01-02T10:00:00Z", "HOLD");
        HoldResponse expected= new HoldResponse("123", Instant.parse("2025-01-01T10:02:00Z"));
        when(jdbc.insertHold(eq("L1"), any(), any())).thenReturn(expected);

        HoldResponse actual = svc.createHold(req);

        assertThat(actual).isEqualTo(expected);
        verify(jdbc).insertHold(eq("L1"),
                eq(Instant.parse("2025-01-01T10:00:00Z")),
                eq(Instant.parse("2025-01-02T10:00:00Z")));

    }

    @Test
    void createHold_overlap_mapsTo409() {
        HoldRequest req = new HoldRequest("L1", "2025-01-01T10:00:00Z", "2025-01-02T10:00:00Z", "HOLD");
        when(jdbc.insertHold(anyString(),any(), any())).thenThrow(new DataAccessException("dup"){});

        assertThatThrownBy(() -> svc.createHold(req)) // run service method, but expect it to throw an exception
                .isInstanceOf(ResponseStatusException.class)// Ensure the exception is ResponseStatusException class
                .extracting(ex -> ((ResponseStatusException) ex).getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void firmHold_delegateWithDurationMinutes() {
        svc.firmHold(123L, "B77", 30);
        ArgumentCaptor<Duration> cap = ArgumentCaptor.forClass(Duration.class);
        verify(jdbc).firmHold(eq(123L), eq("B77"), cap.capture());
        assertThat(cap.getValue()).isEqualTo(Duration.ofMinutes(30));
    }

    @Test
    void release_isIdempotentCall() {
        svc.release(123L);
        verify(jdbc).releaseHold(123L);
    }
}
