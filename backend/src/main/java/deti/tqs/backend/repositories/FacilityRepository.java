package deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import deti.tqs.backend.models.Facility;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
  
}
