package com.carrental.availability.controller;


import com.carrental.availability.dao.AvailabilityJdbc;
import com.carrental.availability.dto.BusyWindow;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {
    private final AvailabilityJdbc repo;
    public AvailabilityController(AvailabilityJdbc repo){
        this.repo = repo;
    }


    // Busy windows for calendar shading
    @GetMapping("/{listingId")
    List<BusyWindow> search(
            @PathVariable String listingId,
            @RequestParam String fromUtc,
            @RequestParam String toUtc) {
        return repo.findBusy(listingId, Instant.parse(fromUtc), Instant.parse(toUtc));
    }

    // True/false check with turnaround buffers (minutes)
    @GetMapping("/{listingId}/check")
    Map<String, Object> check(
            @PathVariable String listingId,
            @RequestParam String fromUtc,
            @RequestParam String toUtc) {
        boolean ok = repo.isAvailable(
                listingId, Instant.parse(fromUtc), Instant.parse(toUtc));

        return Map.of("available", ok);
    }



//    @PostMapping("/hold")
//    public HoldResponse create(@RequestBody HoldRequest r){
//        return service.createHold(r);
//    }
//
//    @PostMapping("/{id}/firm")
//    public void firm(@PathVariable Long id, @RequestParam String bookingId){
//        service.firm(id, bookingId);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 recommended for deletes
//    public void release(@PathVariable Long id){
//        service.release(id);
//    }
}
