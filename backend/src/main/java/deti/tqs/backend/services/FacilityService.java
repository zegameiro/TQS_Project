package deti.tqs.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.tqs.backend.models.Facility;
import deti.tqs.backend.repositories.FacilityRepository;

@Service
public class FacilityService {

  private FacilityRepository facilityRepository;

  @Autowired
  public FacilityService(FacilityRepository facilityRepository) {
    this.facilityRepository = facilityRepository;
  }

  public Facility save(Facility facility) {
    return facilityRepository.save(facility);
  }

  public Facility getFacilityById(long id) {
    return facilityRepository.findById(id);
  }

  public List<Facility> getAllFacilities() {
    return facilityRepository.findAll();
  }
  
}
