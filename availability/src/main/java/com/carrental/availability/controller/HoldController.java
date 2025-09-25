package com.carrental.availability.controller;

import com.carrental.availability.dto.HoldRequest;
import com.carrental.availability.dto.HoldResponse;
import com.carrental.availability.service.HoldService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hold")
public class HoldController {
    private final HoldService svc;

    public HoldController(HoldService svc){
        this.svc = svc;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HoldResponse createHold(@RequestBody HoldRequest r){
        return svc.createHold(r);
    }

    @PostMapping("/{holdId}/firm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void firmHold(
            @PathVariable Long holdId,
            @RequestParam String bookingId,
            @RequestParam(defaultValue = "120") int cleanupTimeMinute){
        svc.firmHold(holdId, bookingId, cleanupTimeMinute);
    }

    @PostMapping("/{holdId}/release")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void releaseHold(@PathVariable Long holdId){
        svc.release(holdId);
    }
}
