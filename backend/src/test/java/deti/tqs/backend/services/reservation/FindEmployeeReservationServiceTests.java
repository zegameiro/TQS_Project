package deti.tqs.backend.services.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
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

import deti.tqs.backend.dtos.ReservationSchema;
import deti.tqs.backend.models.Employee;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.repositories.EmployeeRepository;
import deti.tqs.backend.services.ReservationService;

@ExtendWith(MockitoExtension.class)
class FindEmployeeReservationServiceTests {
  
  @Mock
  private EmployeeRepository employeeRepository;

  @InjectMocks
  private ReservationService reservationService;

  /*
   * NECESSARY TESTS
   * 
   *  1. Find a valid employee for a reservation when the employee doesn't have any reservations
   *  2. Find a valid employee for a reservation when the employee has reservations but none for the same time
   *  3. Fail to find a valid employee for a reservation when the employee has reservations but has one for the same time
   *  4. Pass null atributtes to the method
   *  5. Find a list with no employees
   *  6. Find an employee that doesn't have the speciality required
   *  7. Return a valid employee when one has a reservation at the same time but the other doesn't
   * 
  */

  private Facility facility = new Facility();

  private Room room = new Room();

  private Speciality haircut = new Speciality();
  private Speciality beardtrim = new Speciality();
  private Speciality washing = new Speciality();

  private Employee employee1 = new Employee();
  private Employee employee2 = new Employee();
  private Employee employee3 = new Employee();

  private Reservation reservation1 = new Reservation();
  private Reservation reservation2 = new Reservation();

  @BeforeEach
  void setUp() {

    facility.setName("Plaza");
    facility.setPhoneNumber("726361937");
    facility.setMaxRoomsCapacity(20);
    facility.setPostalCode("4520-123");
    facility.setStreetName("Rua do Plaza");

    room.setName("Room 1");
    room.setMaxChairsCapacity(10);
    room.setBeautyServiceId(1);
    room.setFacility(facility);

    haircut.setName("Haircut");
    haircut.setPrice(10.0);
    haircut.setBeautyServiceId(0);

    beardtrim.setName("Beard Trimming");
    beardtrim.setPrice(5);
    beardtrim.setBeautyServiceId(0);

    washing.setName("Washing");
    washing.setPrice(5);
    washing.setBeautyServiceId(0);

    employee1.setFullName("John Smith");
    employee1.setEmail("jonh@plaza.com");
    employee1.setPhoneNumber("912345678");
    employee1.setSpecialitiesID(List.of(haircut.getId(), beardtrim.getId(), washing.getId()));

    employee2.setFullName("Jane Doe");
    employee2.setEmail("jane@plaza.com");
    employee2.setPhoneNumber("912345678");
    employee2.setSpecialitiesID(List.of(haircut.getId(), beardtrim.getId()));

    employee3.setFullName("Joseph Caliphornia");
    employee3.setEmail("joseph@plaza.com");
    employee3.setPhoneNumber("982673812");
    employee3.setSpecialitiesID(List.of(washing.getId()));

    reservation1.setTimestamp(System.currentTimeMillis());
    reservation1.setCustomerName("Alice Cooper");
    reservation1.setCustomerEmail("alice@gmail.com");
    reservation1.setCustomerPhoneNumber("912345678");
    reservation1.setSecretCode("asijbjnosi434geg");
    reservation1.setSpeciality(beardtrim);
    reservation1.setEmployee(employee1);

    reservation2.setTimestamp(System.currentTimeMillis() + 2400000);
    reservation2.setCustomerName("Bob Dylan");
    reservation2.setCustomerEmail("bob@gmail.com");
    reservation2.setCustomerPhoneNumber("127364129");
    reservation2.setSecretCode("asijasknf");
    reservation2.setSpeciality(haircut);
    reservation2.setEmployee(employee1);

    employee1.setReservations(List.of(reservation1, reservation2));

  }

  @Test
  @DisplayName("Find a valid employee for a reservation when the employee doesn't have any reservations")
  void findEmployeeForReservation_whenEmpNotReservationsTest() {
    
    when(employeeRepository.findByFacility_Id(anyLong())).thenReturn(List.of(employee2, employee3));

    ReservationSchema reservationSchema = new ReservationSchema(
      "163234234",
      "asijbjnosi434geg",
      "Billie Joe",
      "billie@ua.pt",
      "827672024",
      "1",
      "1"
    );

    Employee employee = reservationService.findEmployeeForReservation(reservationSchema, room, haircut);

    assertAll(
      () -> assertThat(employee).isNotNull(),
      () -> assertThat(employee.getFullName()).isEqualTo("Jane Doe"),
      () -> assertThat(employee.getEmail()).isEqualTo("jane@plaza.com"),
      () -> assertThat(employee.getPhoneNumber()).isEqualTo("912345678"),
      () -> assertThat(employee.getSpecialitiesID()).contains(haircut.getId(), beardtrim.getId())
    );

    verify(employeeRepository, times(1)).findByFacility_Id(anyLong());

  }

  @Test
  @DisplayName("Find a valid employee for a reservation when the employee has reservations but none at the same time")
  void findEmployeeForReservation_whenEmpHasNoReservationsForSameTimeTest() {
    
    when(employeeRepository.findByFacility_Id(anyLong())).thenReturn(List.of(employee1));

    ReservationSchema reservationSchema = new ReservationSchema(
      String.valueOf(System.currentTimeMillis() + 4800000),
      "asijbjnosi434geg",
      "Mike Cool",
      "mike@gmail.com",
      "624819273",
      "1",
      "3"
    );

    Employee employee = reservationService.findEmployeeForReservation(reservationSchema, room, haircut);

    assertAll(
      () -> assertThat(employee).isNotNull(),
      () -> assertThat(employee.getFullName()).isEqualTo("John Smith"),
      () -> assertThat(employee.getEmail()).isEqualTo("jonh@plaza.com"),
      () -> assertThat(employee.getPhoneNumber()).isEqualTo("912345678"),
      () -> assertThat(employee.getSpecialitiesID()).contains(haircut.getId(), beardtrim.getId(), washing.getId()),
      () -> assertThat(employee.getReservations()).hasSize(2)
    );

    verify(employeeRepository, times(1)).findByFacility_Id(anyLong());
  
  }

  @Test
  @DisplayName("Fail to find a valid employee for a reservation when the employee has reservations at the same time")
  void failFindEmployeeForReservation_whenEmpHasReservationsForSameTimeTest() {
    
    when(employeeRepository.findByFacility_Id(anyLong())).thenReturn(List.of(employee1));

    ReservationSchema reservationSchema = new ReservationSchema(
      String.valueOf(System.currentTimeMillis() + 1202060),
      "asijbjnosi434geg",
      "Taylor Mendel",
      "taylor@gmail.com",
      "762539102",
      "1",
      "3"
    );

    Employee employee = reservationService.findEmployeeForReservation(reservationSchema, room, haircut);

    assertThat(employee).isNull();

    verify(employeeRepository, times(1)).findByFacility_Id(anyLong());

  }

  @Test
  @DisplayName("Pass null atributtes to the method")
  void passNullAtributtesToMethodTest() {

    ReservationSchema reservationSchema = new ReservationSchema(
      String.valueOf(System.currentTimeMillis() + 1202060),
      "asijbjnosi434geg",
      "Taylor Mendel",
      "taylor@gmail.com",
      "762539102",
      "1",
      "3"
    );
    
    Employee employeeAttempt1 = reservationService.findEmployeeForReservation(reservationSchema, null, haircut);
    Employee employeeAttempt2 = reservationService.findEmployeeForReservation(reservationSchema, room, null);
    Employee employeeAttempt3 = reservationService.findEmployeeForReservation(null, room, haircut);

    assertAll(
      () -> assertThat(employeeAttempt1).isNull(),
      () -> assertThat(employeeAttempt2).isNull(),
      () -> assertThat(employeeAttempt3).isNull()
    );

    verify(employeeRepository, never()).findByFacility_Id(anyLong());

  }

  @Test
  @DisplayName("Find a list with no employees")
  void findListWithNoEmployeesTest() {

    when(employeeRepository.findByFacility_Id(anyLong())).thenReturn(null);

    ReservationSchema reservationSchema = new ReservationSchema(
      String.valueOf(System.currentTimeMillis() + 1202060),
      "asijbjnosi434geg",
      "Taylor Mendel",
      "taylor@gmail.com",
      "762539102",
      "1",
      "3"
    );

    Employee employee = reservationService.findEmployeeForReservation(reservationSchema, room, haircut);

    assertThat(employee).isNull();

    when(employeeRepository.findByFacility_Id(anyLong())).thenReturn(List.of());

    employee = reservationService.findEmployeeForReservation(reservationSchema, room, haircut);

    assertThat(employee).isNull();

    verify(employeeRepository, times(2)).findByFacility_Id(anyLong());

  }

  @Test
  @DisplayName("Find an employee that doesn't have the speciality required")
  void findEmployeeWithoutSpecialityTest() {

    employee3.setSpecialitiesID(null);

    when(employeeRepository.findByFacility_Id(anyLong())).thenReturn(List.of(employee3));

    ReservationSchema reservationSchema = new ReservationSchema(
      String.valueOf(System.currentTimeMillis() + 1202060),
      "asijbjnosi434geg",
      "Taylor Mendel",
      "taylor@gmail.com",
      "762539102",
      String.valueOf(haircut.getId()),
      "3"
    );

    Employee employee = reservationService.findEmployeeForReservation(reservationSchema, room, haircut);

    assertThat(employee).isNull();

    verify(employeeRepository, times(1)).findByFacility_Id(anyLong());

  }

  @Test
  @DisplayName("Return a valid employee when one has a reservation at the same time but the other doesn't")
  void returnValidEmployeeWhenOneHasReservationAtSameTimeTest() {

    when(employeeRepository.findByFacility_Id(anyLong())).thenReturn(List.of(employee1, employee2));

    ReservationSchema reservationSchema = new ReservationSchema(
      String.valueOf(System.currentTimeMillis() + 1202060),
      "asijbjnosi434geg",
      "Taylor Mendel",
      "taylor@gmail.com",
      "762539102",
      String.valueOf(beardtrim.getId()),
      "3"
    );

    Employee employee = reservationService.findEmployeeForReservation(reservationSchema, room, beardtrim);

    assertAll(
      () -> assertThat(employee).isNotNull(),
      () -> assertThat(employee.getFullName()).isEqualTo("Jane Doe"),
      () -> assertThat(employee.getEmail()).isEqualTo("jane@plaza.com"),
      () -> assertThat(employee.getPhoneNumber()).isEqualTo("912345678"),
      () -> assertThat(employee.getSpecialitiesID()).contains(haircut.getId(), beardtrim.getId())
    );

    verify(employeeRepository, times(1)).findByFacility_Id(anyLong());

  }


}
