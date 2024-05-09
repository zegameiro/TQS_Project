package deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import deti.tqs.backend.models.Speciality;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
  
}
