package com.carrental.availability.dao;

import com.carrental.availability.model.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SlotRepo extends JpaRepository<AvailabilitySlot, Long> {
    Optional<AvailabilitySlot> findByIdAndKind(Long id, String kind);
}
