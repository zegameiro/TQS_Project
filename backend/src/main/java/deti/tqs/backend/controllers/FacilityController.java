package deti.tqs.backend.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import deti.tqs.backend.dtos.FacilitySchema;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.services.FacilityService;

@RestController
@RequestMapping("/api/facility")
public class FacilityController {

  private static final Logger logger = LoggerFactory.getLogger(FacilityController.class);
  
  private final FacilityService facilityService;

  @Autowired
  public FacilityController(FacilityService facilityService) {
    this.facilityService = facilityService;
  }

  @PostMapping("/admin/add")
  public ResponseEntity<Facility> createFacility(@RequestBody(required = true) FacilitySchema facilitySchema) {

    logger.info("Creating facility");

    if(facilitySchema.name() == null || facilitySchema.city() == null || facilitySchema.streetName() == null || facilitySchema.postalCode() == null || facilitySchema.phoneNumber() == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing parameters");

    Facility f = new Facility();

    f.setName(facilitySchema.name());
    f.setCity(facilitySchema.city());
    f.setStreetName(facilitySchema.streetName());
    f.setPostalCode(facilitySchema.postalCode());
    f.setPhoneNumber(facilitySchema.phoneNumber());

    Facility savedFacility = null;

    try {
      
      savedFacility = facilityService.save(f);
      
    } catch (IllegalArgumentException e) {

      return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

    }

    return ResponseEntity.status(HttpStatus.CREATED).body(savedFacility);

  }

  @PutMapping("/admin/update")
  public ResponseEntity<Facility> updateFacility(@RequestBody(required = true) FacilitySchema facilitySchema, @RequestParam(required = true) long id) {

    logger.info("Updating facility");

    Facility f = new Facility();
    f.setName(facilitySchema.name());
    f.setCity(facilitySchema.city());
    f.setStreetName(facilitySchema.streetName());
    f.setPostalCode(facilitySchema.postalCode());
    f.setPhoneNumber(facilitySchema.phoneNumber());

    Facility updatedFacility = null;

    try {
      
      updatedFacility = facilityService.update(f, id);
      
    } catch (IllegalArgumentException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    }

    return ResponseEntity.status(HttpStatus.OK).body(updatedFacility);

  }

  @DeleteMapping("/admin/delete")
  public ResponseEntity<Void> deleteFacility(@RequestParam(required = true) long id) {

    logger.info("Deleting facility");

    try {
      
      facilityService.delete(id);
      
    } catch (IllegalArgumentException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    }

    return ResponseEntity.status(HttpStatus.OK).body(null);

  }

  @GetMapping("/{id}")
  public ResponseEntity<Facility> getFacility(@PathVariable long id) {

    Facility foundFacility = facilityService.getFacilityById(id);

    if (foundFacility == null)
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    return ResponseEntity.status(HttpStatus.OK).body(foundFacility);

  }

  @GetMapping("/all")
  public ResponseEntity<Iterable<Facility>> getAllFacilities() {

    List<Facility> facilities = facilityService.getAllFacilities();

    return ResponseEntity.status(HttpStatus.OK).body(facilities);

  }

}
