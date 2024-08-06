package org.hotel.hotel.repository;

import org.hotel.hotel.entity.AdminDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminDetails,Long> {

    Boolean existsByEmail(String email);
    Optional<AdminDetails> findByEmail(String email);
}
