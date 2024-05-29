package deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.backend.models.Speciality;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long>{
  
  Speciality findById(long id);

  Speciality findByName(String name);

}
