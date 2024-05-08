package deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import deti.tqs.backend.models.Chair;

public interface ChairRepository extends JpaRepository<Chair, Long> {
  
}
