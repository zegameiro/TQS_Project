package deti.tqs.backend.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    Facility f = new Facility();

    f.setName(facilitySchema.name());
    f.setCity(facilitySchema.city());
    f.setStreetName(facilitySchema.streetName());
    f.setPostalCode(facilitySchema.postalCode());
    f.setPhoneNumber(facilitySchema.phoneNumber());

    Facility savedFacility = facilityService.save(f);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedFacility);

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

  @GetMapping("/test")
  public ResponseEntity<String> test() {
    logger.info("ENTERRED TEST");
    return ResponseEntity.status(HttpStatus.OK).body("Test");
  }


}
