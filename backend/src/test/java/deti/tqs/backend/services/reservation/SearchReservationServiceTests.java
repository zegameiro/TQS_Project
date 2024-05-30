package deti.tqs.backend.services.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties.Server.Spec;

import deti.tqs.backend.models.Employee;
import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.repositories.ReservationRepository;
import deti.tqs.backend.services.ReservationService;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class SearchReservationServiceTests {
  
  @Mock
  private ReservationRepository reservationRepository;

  @InjectMocks
  private ReservationService reservationService;

  private Speciality haircut = new Speciality();
  private Speciality manicure = new Speciality();
  private Speciality pedicure = new Speciality();

  private Employee employee = new Employee();

  private Reservation reservation1 = new Reservation();
  private Reservation reservation2 = new Reservation();
  private Reservation reservation3 = new Reservation();
  private Reservation reservation4 = new Reservation();
  private Reservation reservation5 = new Reservation();


  /*
   * NECESSARY TESTS
   * 
   *  1. Find a reservation by its id with success
   *  2. Find a reservation by its id with an Id that doesn't exists
   *  3. Find all reservations with success
   *  4. Find all reservations associate to a customer's email
   *  5. Find all reservations associate to a customer's email that doesn't exists
   *  6. Find all reservations associate to a employee's id
   *  7. Find all reservations associate to a employee's id that doesn't exists
   *  8. Find a reservation by its secret code with success
   *  9. Find a reservation by its secret code with a code that doesn't exists
   * 
  */

  @BeforeEach
  void setUp() {

    haircut.setId(1L);
    haircut.setName("Haircut");
    haircut.setPrice(30.2);
    haircut.setBeautyServiceId(0);

    manicure.setId(2L);
    manicure.setName("Manicure");
    manicure.setPrice(15.0);
    manicure.setBeautyServiceId(4);

    pedicure.setId(3L);
    pedicure.setName("Pedicure");
    pedicure.setPrice(20.0);
    pedicure.setBeautyServiceId(4);

    employee.setId(1L);
    employee.setFullName("Joseph Smith");
    employee.setEmail("joseph@plaza.pt");
    employee.setPhoneNumber("728316730");
    employee.setSpecialitiesID(List.of(manicure.getId(), haircut.getId()));

    reservation1.setId(1L);
    reservation1.setCustomerName("Jonh Doe");
    reservation1.setCustomerEmail("john@gmail.com");
    reservation1.setCustomerPhoneNumber("123456789");
    reservation1.setSecretCode("ahsgjelqof");
    reservation1.setTimestamp(System.currentTimeMillis());
    reservation1.setSpeciality(haircut);
    reservation1.setEmployee(employee);

    reservation2.setId(2L);
    reservation2.setCustomerName("Jane Doe");
    reservation2.setCustomerEmail("jane@gmail.com");
    reservation2.setCustomerPhoneNumber("987654321");
    reservation2.setSecretCode("utyen12mc");
    reservation2.setTimestamp(System.currentTimeMillis() + 102984455);
    reservation2.setSpeciality(pedicure);

    reservation3.setId(3L);
    reservation3.setCustomerName("Stan Phillips");
    reservation3.setCustomerEmail("stan@ua.pt");
    reservation3.setCustomerPhoneNumber("748361830");
    reservation3.setSecretCode("qivnjweoih");
    reservation3.setTimestamp(System.currentTimeMillis() + 283942);
    reservation3.setSpeciality(manicure);
    reservation3.setEmployee(employee);

    reservation4.setId(4L);
    reservation4.setCustomerName("John Doe");
    reservation4.setCustomerEmail("john@gmail.com");
    reservation4.setCustomerPhoneNumber("123456789");
    reservation4.setSecretCode("qwejklqwe");
    reservation4.setTimestamp(System.currentTimeMillis() + 123456);
    reservation4.setSpeciality(pedicure);

    reservation5.setId(5L);
    reservation5.setCustomerName("John Doe");
    reservation5.setCustomerEmail("john@gmail.com");
    reservation5.setCustomerPhoneNumber("123456789");
    reservation5.setSecretCode("128fsdbhqowi");
    reservation5.setTimestamp(System.currentTimeMillis() + 34124567);
    reservation5.setSpeciality(manicure);
    reservation5.setEmployee(employee);
    
    employee.setReservations(List.of(reservation1, reservation3, reservation5));

  }

  @Test
  @DisplayName("Find a reservation by its id with success")
  void whenValidId_thenReservationShouldBeFound() {

    when(reservationRepository.findById(anyLong())).thenReturn(reservation1);

    Reservation found = reservationService.getReservation(1L);

    assertAll(
      () -> assertThat(found).isNotNull().isEqualTo(reservation1),
      () -> assertThat(found.getValidityID()).isZero()
    );

    verify(reservationRepository, times(1)).findById(anyLong());
    
  }

  @Test
  @DisplayName("Find a reservation by its id with an Id that doesn't exists")
  void whenInvalidId_thenReservationShouldNotBeFound() {

    when(reservationRepository.findById(anyLong())).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> reservationService.getReservation(10L));

    verify(reservationRepository, times(1)).findById(anyLong());

  }



}
