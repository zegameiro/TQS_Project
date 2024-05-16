package deti.tqs.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import deti.tqs.backend.models.Facility;
import deti.tqs.backend.repositories.FacilityRepository;

@Service
public class FacilityService {

  private FacilityRepository facilityRepository;

  public FacilityService(FacilityRepository facilityRepository) {
    this.facilityRepository = facilityRepository;
  }

  public Facility save(Facility facility) {

    Facility found = facilityRepository.findByName(facility.getName());

    if (found != null)
      throw new IllegalArgumentException("Facility with this name already exists");

    return facilityRepository.save(facility);

  }

  public Facility getFacilityById(long id) {

    return facilityRepository.findById(id);

  }

  public List<Facility> getAllFacilities() {

    return facilityRepository.findAll();

  }

  public Facility getFacilityByName(String name) {

    return facilityRepository.findByName(name);

  }
  
}
