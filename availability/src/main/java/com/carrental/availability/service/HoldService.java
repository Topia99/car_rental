package com.carrental.availability.service;


import com.carrental.availability.dao.AvailabilityJdbc;
import com.carrental.availability.dto.HoldRequest;
import com.carrental.availability.dto.HoldResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;

@Service
public class HoldService {
    private final AvailabilityJdbc repo;

    public HoldService(AvailabilityJdbc repo){
        this.repo = repo;
    }

    @Transactional
    public HoldResponse createHold(HoldRequest r){
        try {
            return repo.insertHold(r.listingId(),
                    Instant.parse(r.fromUtc()),
                    Instant.parse(r.toUtc()));
        } catch (DataAccessException e) {
            // overlap -> constraint violation
            throw new ResponseStatusException(HttpStatus.CONFLICT, "overlap");
        }
    }

    @Transactional
    public void firmHold(Long holdId, String bookingId, int cleanupMinutes) {
        repo.firmHold(holdId, bookingId, Duration.ofMinutes(cleanupMinutes));
    }

    @Transactional
    public void release(Long holdId){
        repo.releaseHold(holdId);
    }
}
