package deti.tqs.backend.repositories.speciality;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.repositories.ReservationRepository;
import deti.tqs.backend.repositories.SpecialityRepository;

@DataJpaTest
class CreateSpecialityRepositoryTests {
  
  private SpecialityRepository specialityRepository;

  private ReservationRepository reservationRepository;

  @Autowired
  CreateSpecialityRepositoryTests(SpecialityRepository specialityRepository, ReservationRepository reservationRepository) {
    this.specialityRepository = specialityRepository;
    this.reservationRepository = reservationRepository;
  }

  /*
   * NECESSARY TESTS
   * 
   *  1. Create a speciality with success
   * 
  */

  @Test
  @DisplayName("Create a speciality with success")
  void createSpecialityWithSuccess() {

    Reservation reservation = new Reservation();
    reservation.setTimestamp(System.currentTimeMillis());
    reservation.setSecretCode("absklihfuiowini");
    reservation.setCustomerName("John Doe");
    reservation.setCustomerEmail("john.doe@gmail.com");
    reservation.setCustomerPhoneNumber("123456789");


    Speciality speciality = new Speciality();
    speciality.setName("Haircut");
    speciality.setBeautyServiceId(1);
    speciality.setPrice(29.21);

    reservation.setSpeciality(speciality);

    reservationRepository.save(reservation);

    Speciality spec = specialityRepository.save(speciality);

    assertThat(spec).isNotNull().isEqualTo(speciality);
  }

}
