package deti.tqs.backend.services.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.repositories.ReservationRepository;
import deti.tqs.backend.services.ReservationService;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class UpdateReservationServiceTests {
  
  @Mock
  private ReservationRepository reservationRepository;

  @InjectMocks
  private ReservationService reservationService;

  private Reservation reservation = new Reservation();

  /*
   * NECESSARY TESTS
   * 
   *  1. Update a reservation status with success for each case
   *  2. Update a reservation status that doesn't exists
   *  3. Update a reservation status with an invalid status
   * 
  */

  @BeforeEach
  void setUp() {

    reservation.setCustomerName("John Doe");
    reservation.setCustomerEmail("john@gmail.com");
    reservation.setCustomerPhoneNumber("912345678");
    reservation.setSecretCode("122ujksdjkf");
    reservation.setTimestamp(System.currentTimeMillis());

  }

  @Test
  @DisplayName("Update a reservation with success 1")
  void updateReservationWithSuccess1() {

    reservation.setValidityID(0);

    when(reservationRepository.findById(anyLong())).thenReturn(reservation);
    when(reservationRepository.save(reservation)).thenReturn(reservation);

    Reservation updatedReservation = reservationService.updateReservationStatus(reservation.getId(), "0");

    assertAll(
      () -> assertThat(updatedReservation).isNotNull(),
      () -> assertThat(updatedReservation.getValidityID()).isZero()
    );

    verify(reservationRepository, times(1)).findById(anyLong());
    verify(reservationRepository, times(1)).save(reservation);

  }

  @Test
  @DisplayName("Update a reservation with success 2")
  void updateReservationWithSuccess2() {

    reservation.setValidityID(1);

    when(reservationRepository.findById(anyLong())).thenReturn(reservation);
    when(reservationRepository.save(reservation)).thenReturn(reservation);

    Reservation updatedReservation = reservationService.updateReservationStatus(reservation.getId(), "1");

    assertAll(
      () -> assertThat(updatedReservation).isNotNull(),
      () -> assertThat(updatedReservation.getValidityID()).isEqualTo(1)
    );

    verify(reservationRepository, times(1)).findById(anyLong());
    verify(reservationRepository, times(1)).save(reservation);

  }

  @Test
  @DisplayName("Update a reservation with success 3")
  void updateReservationWithSuccess3() {

    reservation.setValidityID(2);

    when(reservationRepository.findById(anyLong())).thenReturn(reservation);
    when(reservationRepository.save(reservation)).thenReturn(reservation);

    Reservation updatedReservation = reservationService.updateReservationStatus(reservation.getId(), "2");

    assertAll(
      () -> assertThat(updatedReservation).isNotNull(),
      () -> assertThat(updatedReservation.getValidityID()).isEqualTo(2)
    );

    verify(reservationRepository, times(1)).findById(anyLong());
    verify(reservationRepository, times(1)).save(reservation);

  }

  @Test
  @DisplayName("Update a reservation with success 4")
  void updateReservationWithSuccess4() {

    reservation.setValidityID(3);

    when(reservationRepository.findById(anyLong())).thenReturn(reservation);
    when(reservationRepository.save(reservation)).thenReturn(reservation);

    Reservation updatedReservation = reservationService.updateReservationStatus(reservation.getId(), "3");

    assertAll(
      () -> assertThat(updatedReservation).isNotNull(),
      () -> assertThat(updatedReservation.getValidityID()).isEqualTo(3)
    );

    verify(reservationRepository, times(1)).findById(anyLong());
    verify(reservationRepository, times(1)).save(reservation);

  }

  @Test
  @DisplayName("Update a reservation with success 5")
  void updateReservationWithSuccess5() {

    reservation.setValidityID(4);

    when(reservationRepository.findById(anyLong())).thenReturn(reservation);
    when(reservationRepository.save(reservation)).thenReturn(reservation);

    Reservation updatedReservation = reservationService.updateReservationStatus(reservation.getId(), "4");

    assertAll(
      () -> assertThat(updatedReservation).isNotNull(),
      () -> assertThat(updatedReservation.getValidityID()).isEqualTo(4)
    );

    verify(reservationRepository, times(1)).findById(anyLong());
    verify(reservationRepository, times(1)).save(reservation);

  }

  @Test
  @DisplayName("Update a reservation that doesn't exists")
  void updateReservationThatDoesntExists() {

    when(reservationRepository.findById(anyLong())).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> reservationService.updateReservationStatus(1L, "0"));

    verify(reservationRepository, times(1)).findById(anyLong());
    verify(reservationRepository, never()).save(any());

  }

  @Test
  @DisplayName("Update a reservation with an invalid status")
  void updateReservationWithInvalidStatus() {

    when(reservationRepository.findById(anyLong())).thenReturn(reservation);

    assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(reservation.getId(), "5"));

    verify(reservationRepository, times(1)).findById(anyLong());
    verify(reservationRepository, never()).save(any());

  }

}
