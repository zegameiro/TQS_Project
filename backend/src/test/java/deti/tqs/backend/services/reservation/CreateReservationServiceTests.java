package deti.tqs.backend.services.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.backend.dtos.ReservationSchema;
import deti.tqs.backend.models.Employee;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.models.ReservationQueue;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.repositories.EmployeeRepository;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.ReservationRepository;
import deti.tqs.backend.repositories.RoomRepository;
import deti.tqs.backend.repositories.SpecialityRepository;
import deti.tqs.backend.services.ReservationService;

@ExtendWith(MockitoExtension.class)
class CreateReservationServiceTests {
  
  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private RoomRepository roomRepository;

  @Mock
  private SpecialityRepository specialityRepository;

  @Mock
  private FacilityRepository facilityRepository;

  @Mock
  private EmployeeRepository employeeRepository;

  @InjectMocks
  private ReservationService reservationService;

  private Room room1 = new Room();

  private Facility facility1 = new Facility();

  private Speciality speciality1 = new Speciality();

  private ReservationSchema reservationSchema1;

  private ReservationQueue queue1 = new ReservationQueue();

  private Employee employee1 = new Employee();

  private Reservation reservation1 = new Reservation();

  /*
   * NECESSARY TESTS
   * 
   *  1. Create a valid Reservation
   *  2. Create a Reservation with a room that does not exists
   *  3. Create a Reservation with a speciality that does not exists
   *  4. Create a Reservation and assign it to an employee that doesn't have availability
   *  5. Create a Reservation and assign it to a non existing queue
   * 
  */

  @BeforeEach
  void setUp() {

    facility1.setName("Facility 1");
    facility1.setCity("Aveiro");
    facility1.setMaxRoomsCapacity(10);
    facility1.setPhoneNumber("123456789");
    facility1.setPostalCode("3810-193");
    facility1.setStreetName("Rua de Aveiro");

    room1.setName("Room 1");
    room1.setMaxChairsCapacity(10);
    room1.setFacility(facility1);
    room1.setBeautyServiceId(2);

    queue1.setFacility(facility1);
    queue1.setReservations(new ArrayList<>());

    facility1.setReservationQueue(queue1);
    facility1.setRooms(List.of(room1));

    speciality1.setName("Speciality 1");
    speciality1.setPrice(10.0);
    speciality1.setBeautyServiceId(2);

    employee1.setAdmin(false);
    employee1.setFullName("Francis Smith");
    employee1.setEmail("francis.smith@plaza.pt");
    employee1.setPhoneNumber("987654321");
    employee1.setSpecialities(List.of(speciality1));
    employee1.setReservations(new ArrayList<>());
    employee1.setFacility(facility1);

    employeeRepository.saveAndFlush(employee1);

    facility1.setEmployees(List.of(employee1));

    facilityRepository.saveAndFlush(facility1);

    reservationSchema1 = new ReservationSchema(
      "123456789",
      "askjwoevn", 
      "John Doe",
      "johndoe@gmail.com",
      "987654321",
      "1",
      "1"
    );

    reservation1.setTimestamp(123456789);
    reservation1.setSecretCode("askjwoevn");
    reservation1.setCustomerName("John Doe");
    reservation1.setCustomerEmail("johndoe@gmail.com");
    reservation1.setCustomerPhoneNumber("987654321");
    reservation1.setSpeciality(speciality1);
    reservation1.setRoom(room1);
    reservation1.setEmployee(employee1);
    reservation1.setReservationQueue(queue1);

  }

  @Test
  @DisplayName("When creating a valid Reservation, it should return the saved Reservation")
  void testCreateValidReservation() {

    when(roomRepository.findById(anyLong())).thenReturn(room1);
    when(specialityRepository.findById(anyLong())).thenReturn(speciality1);
    when(reservationRepository.save(any())).thenReturn(reservation1);

    Reservation createdReservation = reservationService.createReservation(reservationSchema1);

    assertAll(
      () -> assertThat(createdReservation).isNotNull(),
      () -> assertThat(createdReservation.getTimestamp()).isEqualTo(123456789),
      () -> assertThat(createdReservation.getCustomerName()).isEqualTo("John Doe"),
      () -> assertThat(createdReservation.getCustomerEmail()).isEqualTo("johndoe@gmail.com"),
      () -> assertThat(createdReservation.getCustomerPhoneNumber()).isEqualTo("987654321"),
      () -> assertThat(createdReservation.getSecretCode()).isEqualTo("askjwoevn"),
      () -> assertThat(createdReservation.getRoom()).isEqualTo(room1),
      () -> assertThat(createdReservation.getSpeciality()).isEqualTo(speciality1),
      () -> assertThat(createdReservation.getEmployee()).isEqualTo(employee1)
    );

    verify(roomRepository, times(1)).findById(anyLong());
    verify(specialityRepository, times(1)).findById(anyLong());
    verify(reservationRepository, times(1)).save(createdReservation);

  }

}
