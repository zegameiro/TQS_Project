package deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.backend.models.Chair;

@Repository
public interface ChairRepository extends JpaRepository<Chair, Long> {
  
  Chair findById(long id);

}
