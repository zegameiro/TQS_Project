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

  public Facility update(Facility facility, long id) {

    Facility found = facilityRepository.findById(id);

    if (found == null)
      throw new IllegalArgumentException("Facility not found");

    found.setName(facility.getName() != found.getName() ? facility.getName() : found.getName());
    found.setCity(facility.getCity() != found.getCity() ? facility.getCity() : found.getCity());
    found.setStreetName(facility.getStreetName() != found.getStreetName() ? facility.getStreetName() : found.getStreetName());
    found.setPhoneNumber(facility.getPhoneNumber() != found.getPhoneNumber() ? facility.getPhoneNumber() : found.getPhoneNumber());
    found.setPostalCode(facility.getPostalCode() != found.getPostalCode() ? facility.getPostalCode() : found.getPostalCode());

    return facilityRepository.save(found);

  }

  public void delete(long id) {
      
    Facility found = facilityRepository.findById(id);

    if (found == null)
      throw new IllegalArgumentException("Facility not found");

    facilityRepository.delete(found);

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
