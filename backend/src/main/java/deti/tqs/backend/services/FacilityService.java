package deti.tqs.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.tqs.backend.models.Facility;
import deti.tqs.backend.repositories.FacilityRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class FacilityService {

  private FacilityRepository facilityRepository;

  @Autowired
  FacilityService(FacilityRepository facilityRepository) {
    this.facilityRepository = facilityRepository;
  }

  public Facility save(Facility facility) throws Exception {

    Facility found = facilityRepository.findByName(facility.getName());
    
    // Check if a facility with the same name already exists
    if (found != null)
      throw new EntityExistsException("Facility with this name already exists");

    // Check if there are some fields missing
    if(facility.getName() == null || facility.getCity() == null || facility.getPhoneNumber() == null || facility.getPostalCode() == null || facility.getStreetName() == null)
      throw new NoSuchFieldException("Facility must have all fields filled");

    // Check if the capacity has a valid value
    if(facility.getMaxRoomsCapacity() <= 0)
      throw new IllegalArgumentException("Facility must have a valid capacity digit greater than 0");

    return facilityRepository.save(facility);

  }

  public Facility update(Facility facility, long id) {

    Facility found = facilityRepository.findById(id);

    if (found == null)
      throw new EntityNotFoundException("Facility not found");
    
    if(facility.getMaxRoomsCapacity() <= 0)
      throw new IllegalArgumentException("Facility must have a valid capacity digit greater than 0");

    found.setName(facility.getName() != found.getName() ? facility.getName() : found.getName());
    found.setCity(facility.getCity() != found.getCity() ? facility.getCity() : found.getCity());
    found.setStreetName(facility.getStreetName() != found.getStreetName() ? facility.getStreetName() : found.getStreetName());
    found.setPhoneNumber(facility.getPhoneNumber() != found.getPhoneNumber() ? facility.getPhoneNumber() : found.getPhoneNumber());
    found.setPostalCode(facility.getPostalCode() != found.getPostalCode() ? facility.getPostalCode() : found.getPostalCode());
    found.setMaxRoomsCapacity(facility.getMaxRoomsCapacity() != found.getMaxRoomsCapacity() ? facility.getMaxRoomsCapacity() : found.getMaxRoomsCapacity());

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
