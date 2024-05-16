package deti.tqs.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import deti.tqs.backend.models.Facility;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
  
  Facility findById(long id);

  @Query("SELECT DISTINCT f.city FROM Facility f")
  List<String> findCities();

  @Query("SELECT f FROM Facility f WHERE f.city LIKE %?1%")
  List<Facility> findByCity(String city);

  Facility findByName(String name);

}
