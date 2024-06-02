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

import deti.tqs.backend.dtos.FacilitySchema;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.services.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/facility")
@Tag(name = "Facility", description = "Operations pertaining to facilities in the system.")
public class FacilityController {

  private static final Logger logger = LoggerFactory.getLogger(FacilityController.class);
  
  private final FacilityService facilityService;

  @Autowired
  public FacilityController(FacilityService facilityService) {
    this.facilityService = facilityService;
  }

  @PostMapping("/admin/add")
  @Operation(summary = "Create a new facility", description = "An admin create a new facility in the system.")
  public ResponseEntity<Facility> createFacility(@RequestBody(required = true) FacilitySchema facilitySchema) throws Exception {

    if (facilitySchema.name() == null || facilitySchema.city() == null || facilitySchema.phoneNumber() == null || facilitySchema.postalCode() == null || facilitySchema.streetName() == null)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

    logger.info("Creating facility");

    Facility f = new Facility();

    f.setName(facilitySchema.name());
    f.setCity(facilitySchema.city());
    f.setStreetName(facilitySchema.streetName());
    f.setPostalCode(facilitySchema.postalCode());
    f.setPhoneNumber(facilitySchema.phoneNumber());
    f.setMaxRoomsCapacity(facilitySchema.maxRoomsCapacity());

    Facility savedFacility = null;

    try {
      
      savedFacility = facilityService.save(f);  

    } catch (EntityExistsException e) {

      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
      
    } catch (IllegalArgumentException e) {

      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

    }

    return ResponseEntity.status(HttpStatus.CREATED).body(savedFacility);

  }

  @PutMapping("/admin/update")
  @Operation(summary = "Update a facility", description = "An admin update a facility in the system.")
  public ResponseEntity<Facility> updateFacility(@RequestBody(required = true) FacilitySchema facilitySchema, @RequestParam(required = true) long id) {

    logger.info("Updating facility");

    Facility f = new Facility();
    f.setName(facilitySchema.name());
    f.setCity(facilitySchema.city());
    f.setStreetName(facilitySchema.streetName());
    f.setPostalCode(facilitySchema.postalCode());
    f.setPhoneNumber(facilitySchema.phoneNumber());
    f.setMaxRoomsCapacity(facilitySchema.maxRoomsCapacity());

    Facility updatedFacility = null;

    try {
      
      updatedFacility = facilityService.update(f, id);
      
    } catch (EntityNotFoundException e) {

      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    }

    return ResponseEntity.status(HttpStatus.OK).body(updatedFacility);

  }

  @DeleteMapping("/admin/delete")
  @Operation(summary = "Delete a facility", description = "An admin delete a facility in the system.")
  public ResponseEntity<Void> deleteFacility(@RequestParam(required = true) long id) {

    logger.info("Deleting facility");

    try {
      
      facilityService.delete(id);
      
    } catch (IllegalArgumentException e) {

      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    }

    return ResponseEntity.status(HttpStatus.OK).body(null);

  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a facility", description = "Get a facility by id.")
  public ResponseEntity<Facility> getFacility(@PathVariable long id) {

    Facility foundFacility = facilityService.getFacilityById(id);

    if (foundFacility == null)
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    return ResponseEntity.status(HttpStatus.OK).body(foundFacility);

  }

  @GetMapping("/all")
  @Operation(summary = "Get all facilities", description = "Get all facilities in the system.")
  public ResponseEntity<Iterable<Facility>> getAllFacilities() {

    List<Facility> facilities = facilityService.getAllFacilities();

    return ResponseEntity.status(HttpStatus.OK).body(facilities);

  }

}
