package deti.tqs.backend.controllers.reservation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.backend.JsonUtils;
import deti.tqs.backend.controllers.ReservationController;
import deti.tqs.backend.dtos.ReservationSchema;
import deti.tqs.backend.models.Employee;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.models.ReservationQueue;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.services.ReservationService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
class CreateReservationControllerTests {
  
  private MockMvc mvc;

  @Autowired
  CreateReservationControllerTests(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private ReservationService reservationService;

  private ReservationSchema reservationSchema1 = null;

  private Facility facility1 = new Facility();

  private Room room1 = new Room();

  private ReservationQueue queue1 = new ReservationQueue();

  private Speciality speciality1 = new Speciality();

  private Employee employee1 = new Employee();

  private Reservation reservation1 = new Reservation();

  /*
   * NECESSARY TESTS
   * 
   *  1. Create a reservation with success
   *  2. Create a reservation with an inexistant room
   *  3. Create a reservation with an inexistant speciality
   *  4. Create a reservation with and not have a employee
   *  5. Pay a reservation with success
   *  6. Pay a reservation that does not exist
   *  7. Pay a reservation with an invalid status
   *  7. Checkin a reservation with success
   *  8. Checkin a reservation that does not exist
   *  9. Checkin a reservation with an invalid status
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

    queue1.setReservations(new ArrayList<>());

    facility1.setReservationQueueId(queue1.getId());
    facility1.setRooms(List.of(room1));

    speciality1.setName("Speciality 1");
    speciality1.setPrice(10.0);
    speciality1.setBeautyServiceId(2);

    employee1.setAdmin(false);
    employee1.setFullName("Francis Smith");
    employee1.setEmail("francis.smith@plaza.pt");
    employee1.setPhoneNumber("987654321");
    employee1.setSpecialitiesID(List.of(speciality1.getId()));
    employee1.setReservations(new ArrayList<>());
    employee1.setFacility(facility1);

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
  @DisplayName("Create a reservation with success")
  void createReservationWithSuccess() throws Exception {

    when(reservationService.createReservation(reservationSchema1)).thenReturn(reservation1);

    mvc.perform(
      post("/api/reservation/add")
      .contentType(MediaType.APPLICATION_JSON)
      .content(JsonUtils.toJson(reservationSchema1))
    )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.timestamp").value(123456789))
      .andExpect(jsonPath("$.secretCode").value("askjwoevn"))
      .andExpect(jsonPath("$.customerName").value("John Doe"))
      .andExpect(jsonPath("$.customerEmail").value("johndoe@gmail.com"))
      .andExpect(jsonPath("$.customerPhoneNumber").value("987654321"));

    verify(reservationService, times(1)).createReservation(reservationSchema1);
  
  }

  @Test
  @DisplayName("Create a reservation with an inexistant room")
  void createReservationWithInexistantRoom() throws Exception {

    ReservationSchema reservationSchema2 = new ReservationSchema(
      "123456789",
      "askjwoevn", 
      "Whatever",
      "whatever@email.com",
      "987654321",
      "1",
      "2937"
    );

    when(reservationService.createReservation(any())).thenThrow(new EntityNotFoundException("Room not found"));

    mvc.perform(
      post("/api/reservation/add")
      .contentType(MediaType.APPLICATION_JSON)
      .content(JsonUtils.toJson(reservationSchema2))
    )
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$").doesNotExist());

    verify(reservationService, times(1)).createReservation(any());

  }

  @Test
  @DisplayName("Create a reservation with an inexistant speciality")
  void createReservationWithInexistantSpeciality() throws Exception {

    ReservationSchema reservationSchema2 = new ReservationSchema(
      "123456789",
      "askjwoevn", 
      "Whatever",
      "whatever@email.com",
      "987654321",
      "2913",
      "2937"
    );

    when(reservationService.createReservation(any())).thenThrow(new EntityNotFoundException("Invalid speciality"));

    mvc.perform(
      post("/api/reservation/add")
      .contentType(MediaType.APPLICATION_JSON)
      .content(JsonUtils.toJson(reservationSchema2))
    )
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$").doesNotExist());

    verify(reservationService, times(1)).createReservation(any());

  }

  @Test
  @DisplayName("Create a reservation with and not have a employee")
  void createReservationWithNoEmployee() throws Exception {

    ReservationSchema reservationSchema2 = new ReservationSchema(
      "123456789",
      "askjwoevn", 
      "Whatever",
      "whatever@email.com",
      "987654321",
      "1",
      "1"
    );

    when(reservationService.createReservation(any())).thenThrow(new IllegalArgumentException("No employee available for the reservation"));

    mvc.perform(
      post("/api/reservation/add")
      .contentType(MediaType.APPLICATION_JSON)
      .content(JsonUtils.toJson(reservationSchema2))
    )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$").doesNotExist());

    verify(reservationService, times(1)).createReservation(any());
  
  }

  @Test
  @DisplayName("Pay a reservation with success")
  void payReservationWithSuccess() throws Exception {

    reservation1.setValidityID(4);

    when(reservationService.updateReservationStatus(anyLong(), anyString())).thenReturn(reservation1);

    mvc.perform(
      post("/api/reservation/pay/1")
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.timestamp").value(123456789))
      .andExpect(jsonPath("$.secretCode").value("askjwoevn"))
      .andExpect(jsonPath("$.customerName").value("John Doe"))
      .andExpect(jsonPath("$.validityID").value(4));

    verify(reservationService, times(1)).updateReservationStatus(anyLong(), anyString());

  }

  @Test
  @DisplayName("Pay a reservation that does not exist")
  void payReservationThatDoesNotExist() throws Exception {

    when(reservationService.updateReservationStatus(anyLong(), anyString())).thenThrow(new EntityNotFoundException("Reservation not found"));

    mvc.perform(
      post("/api/reservation/pay/1")
    )
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$").doesNotExist());

    verify(reservationService, times(1)).updateReservationStatus(anyLong(), anyString());

  }

  @Test
  @DisplayName("Pay a reservation with an invalid status")
  void payReservationWithInvalidStatus() throws Exception {

    when(reservationService.updateReservationStatus(anyLong(), anyString())).thenThrow(new IllegalArgumentException("Invalid status"));

    mvc.perform(
      post("/api/reservation/pay/1")
    )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$").doesNotExist());

    verify(reservationService, times(1)).updateReservationStatus(anyLong(), anyString());

  }

  @Test
  @DisplayName("Checkin a reservation with success")
  void checkinReservationWithSuccess() throws Exception {

    reservation1.setValidityID(1);

    when(reservationService.updateReservationStatus(anyLong(), anyString())).thenReturn(reservation1);

    mvc.perform(
      post("/api/reservation/checkin/1")
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.timestamp").value(123456789))
      .andExpect(jsonPath("$.secretCode").value("askjwoevn"))
      .andExpect(jsonPath("$.customerName").value("John Doe"))
      .andExpect(jsonPath("$.validityID").value(1));

    verify(reservationService, times(1)).updateReservationStatus(anyLong(), anyString());

  }

  @Test
  @DisplayName("Checkin a reservation that does not exist")
  void checkinReservationThatDoesNotExist() throws Exception {

    when(reservationService.updateReservationStatus(anyLong(), anyString())).thenThrow(new EntityNotFoundException("Reservation not found"));

    mvc.perform(
      post("/api/reservation/checkin/1")
    )
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$").doesNotExist());

    verify(reservationService, times(1)).updateReservationStatus(anyLong(), anyString());

  }

  @Test
  @DisplayName("Checkin a reservation with an invalid status")
  void checkinReservationWithInvalidStatus() throws Exception {

    when(reservationService.updateReservationStatus(anyLong(), anyString())).thenThrow(new IllegalArgumentException("Invalid status"));

    mvc.perform(
      post("/api/reservation/checkin/1")
    )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$").doesNotExist());

    verify(reservationService, times(1)).updateReservationStatus(anyLong(), anyString());

  }

}