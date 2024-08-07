package org.hotel.hotel.repository;

import org.hotel.hotel.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedRepository extends JpaRepository<Bed,Long> {
}
