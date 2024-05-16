package deti.tqs.backend.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.backend.models.Facility;

@DataJpaTest
class TestFacilityRepository {
  
  private FacilityRepository facilityRepository;

  @Autowired
  TestFacilityRepository(FacilityRepository facilityRepository) {
    this.facilityRepository = facilityRepository;
  }

  @Test
  @DisplayName("Test save a facility with success and find it by id")
  void testSaveFacilityWithSuccess() {

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
      () -> assertThat(foundFacility.getName()).isEqualTo(f.getName()),
      () -> assertThat(foundFacility.getCity()).isEqualTo(f.getCity()),
      () -> assertThat(foundFacility.getPhoneNumber()).isEqualTo(f.getPhoneNumber()),
      () -> assertThat(foundFacility.getPostalCode()).isEqualTo(f.getPostalCode()),
      () -> assertThat(foundFacility.getStreetName()).isEqualTo(f.getStreetName())
    );

  }

  @Test
  @DisplayName("Test find all the facilities saved")
  void testFindAllSavedFacilities() {

    Facility f1 = new Facility();

    f1.setName("Facility 1");
    f1.setCity("Aveiro");
    f1.setPhoneNumber("123456789");
    f1.setPostalCode("3810-193");
    f1.setStreetName("Rua de Aveiro");

    Facility f2 = new Facility();
    f2.setName("Facility 2");
    f2.setCity("Porto");
    f2.setPhoneNumber("987654321");
    f2.setPostalCode("4000-007");
    f2.setStreetName("Rua do Porto");

    Facility f3 = new Facility();
    f3.setName("Facility 3");
    f3.setCity("Lisboa");
    f3.setPhoneNumber("987523321");
    f3.setPostalCode("1000-072");
    f3.setStreetName("Rua de Lisboa");

    Facility f4 = new Facility();
    f4.setName("Facility 4");
    f4.setCity("Faro");
    f4.setPhoneNumber("987653428");
    f4.setPostalCode("8000-007");
    f4.setStreetName("Rua de Faro");

    facilityRepository.saveAll(List.of(f1, f2, f3, f4));

    List<Facility> facilities = facilityRepository.findAll();

    assertAll(
      () -> assertThat(facilities).isNotNull(),
      () -> assertThat(facilities).hasSize(4),
      () -> assertThat(facilities.get(0).getName()).isEqualTo(f1.getName()),
      () -> assertThat(facilities.get(1).getName()).isEqualTo(f2.getName()),
      () -> assertThat(facilities.get(2).getName()).isEqualTo(f3.getName()),
      () -> assertThat(facilities.get(3).getName()).isEqualTo(f4.getName())
    );

  }

  @Test
  @DisplayName("Test find a facility that doesn't exist")
  void testFindFacilityThatDoesntExist() {

    Facility f = facilityRepository.findById(1L);

    assertThat(f).isNull();
  }

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

  @Test
  @DisplayName("Test find all locations")
  void testFindAllLocations() {

    Facility f1 = new Facility();
    f1.setName("Facility 1");
    f1.setCity("Aveiro");
    f1.setPhoneNumber("123456789");
    f1.setPostalCode("3810-193");
    f1.setStreetName("Rua de Aveiro");

    Facility f2 = new Facility();
    f2.setName("Facility 3");
    f2.setCity("Leiria");
    f2.setPhoneNumber("987523321");
    f2.setPostalCode("1000-072");
    f2.setStreetName("Rua de Lisboa");

    facilityRepository.saveAll(List.of(f1, f2));

    List<String> locations = facilityRepository.findCities();

    assertThat(locations).isNotNull().hasSize(2).containsAll(List.of("Aveiro", "Leiria"));
    
  }

  @Test
  @DisplayName("Test find facilities by city")
  void testFindFacilitiesByCity() {

    Facility f1 = new Facility();
    f1.setName("Facility 1");
    f1.setCity("Aveiro");
    f1.setPhoneNumber("123456789");
    f1.setPostalCode("3810-193");
    f1.setStreetName("Rua de Aveiro");

    Facility f2 = new Facility();
    f2.setName("Facility 3");
    f2.setCity("Leiria");
    f2.setPhoneNumber("987523321");
    f2.setPostalCode("1000-072");
    f2.setStreetName("Rua de Lisboa");

    facilityRepository.saveAll(List.of(f1, f2));

    List<Facility> facilities = facilityRepository.findByCity("ei");
    List<Facility> facilities2 = facilityRepository.findByCity("Le");

    assertAll(
      () -> assertThat(facilities).isNotNull().hasSize(2).containsAll(List.of(f1, f2)),
      () -> assertThat(facilities2).isNotNull().hasSize(1).contains(f2)
    );

  }

}
