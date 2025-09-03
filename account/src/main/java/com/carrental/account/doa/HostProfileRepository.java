package com.carrental.account.doa;

import com.carrental.account.model.HostProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HostProfileRepository extends JpaRepository<HostProfile, Long> {
    Optional<HostProfile> findByUser_id(Long userId);
    boolean existsByUser_Id(Long userId);
}
