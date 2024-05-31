package deti.tqs.backend.repositories.facility;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.backend.models.Facility;
import deti.tqs.backend.repositories.FacilityRepository;

@DataJpaTest
class DeleteFacilityRepositoryTests {

  private FacilityRepository facilityRepository;

  @Autowired
  DeleteFacilityRepositoryTests(FacilityRepository facilityRepository) {
    this.facilityRepository = facilityRepository;
  }

  /*
   * NECESSARY TESTS
   * 
   *  1. Save a facility with success and delete it
   * 
   */

  @Test
  @DisplayName("Test save a facility with success and delete it")
  void testSaveFacilityAndDeleteIt() {
    
    Facility f = new Facility();
    f.setName("Facility 1");
    f.setCity("Aveiro");
    f.setPhoneNumber("123456789");
    f.setPostalCode("3810-193");
    f.setStreetName("Rua de Aveiro");

    facilityRepository.save(f);

    Facility foundFacility = facilityRepository.findById(f.getId());

    assertAll(
      () -> assertThat(foundFacility).isNotNull(),
      () -> assertThat(foundFacility.getName()).isEqualTo(f.getName())
    );

    facilityRepository.delete(f);

    Facility deletedFacility = facilityRepository.findById(f.getId());

    assertThat(deletedFacility).isNull();

  }
  
}
