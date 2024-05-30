package deti.tqs.backend.repositories.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.repositories.ReservationRepository;
import deti.tqs.backend.repositories.RoomRepository;
import deti.tqs.backend.repositories.SpecialityRepository;

@DataJpaTest
class AddReservationRepositoryTests {
  
  private RoomRepository roomRepository;
  private ReservationRepository reservationRepository;
  private SpecialityRepository specialityRepository;

  @Autowired
  AddReservationRepositoryTests(ReservationRepository reservationRepository, SpecialityRepository specialityRepository, RoomRepository roomRepository) {
    this.reservationRepository = reservationRepository;
    this.specialityRepository = specialityRepository;
    this.roomRepository = roomRepository;
  }

  private Room room = new Room();

  private Speciality speciality = new Speciality();

  private Reservation reservation = new Reservation();

  /*
   * NECESSARY TESTS
   * 
   *  1. Save a reservation with success and find it by id
   * 
  */

  @BeforeEach
  void setUp() {

    room.setName("Room 1");
    room.setMaxChairsCapacity(10);
    room.setBeautyServiceId(1);

    roomRepository.save(room);

    speciality.setName("Speciality 1");
    speciality.setPrice(10.0);
    speciality.setBeautyServiceId(2);

    specialityRepository.save(speciality);

    reservation.setTimestamp(System.currentTimeMillis());
    reservation.setSecretCode("absklihfuiowini");
    reservation.setCustomerName("John Doe");
    reservation.setCustomerEmail("john.doe@gmail.com");
    reservation.setCustomerPhoneNumber("123456789");
    reservation.setSpeciality(speciality);
    reservation.setRoom(room);

  }

  @Test
  @DisplayName("Test save a reservation with success and find it by id")
  void testSaveReservationWithSuccess() {

    reservationRepository.save(reservation);

    Reservation foundReservation = reservationRepository.findById(1);

    assertAll(
      () -> assertThat(foundReservation).isNotNull(),
      () -> assertThat(foundReservation.getTimestamp()).isEqualTo(reservation.getTimestamp()),
      () -> assertThat(foundReservation.getSecretCode()).isEqualTo(reservation.getSecretCode()),
      () -> assertThat(foundReservation.getCustomerName()).isEqualTo(reservation.getCustomerName()),
      () -> assertThat(foundReservation.getCustomerEmail()).isEqualTo(reservation.getCustomerEmail()),
      () -> assertThat(foundReservation.getCustomerPhoneNumber()).isEqualTo(reservation.getCustomerPhoneNumber()),
      () -> assertThat(foundReservation.getSpeciality()).isEqualTo(reservation.getSpeciality()),
      () -> assertThat(foundReservation.getRoom()).isEqualTo(reservation.getRoom()),
      () -> assertThat(foundReservation.getValidityID()).isZero()
    );

  }

}
