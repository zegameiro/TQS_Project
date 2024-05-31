package deti.tqs.backend.controllers.reservation;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.backend.controllers.ReservationController;
import deti.tqs.backend.models.Employee;
import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.services.ReservationService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
class SearchReservationControllerTests {

  private MockMvc mvc;

  @Autowired
  SearchReservationControllerTests(MockMvc mvc) {
    this.mvc = mvc;
  }
  
  @MockBean
  private ReservationService reservationService;

  /*
   * NECESSARY TESTS 
   * 
   *  1. Search a reservation with success by its ID
   *  2. Search a reservation that with an ID that doesn't exists
   *  3. Get all the reservations from an employee ID
   *  4. Get all the reservations from an customer email
   *  5. Get a reservation by its secret code
   *  6. Fail to get a reservation by an invalid secret code
   *  7. Get all the reservations
   * 
  */

  private Employee employee = new Employee();

  private Speciality speciality = new Speciality();

  private Reservation reservation1 = new Reservation();
  private Reservation reservation2 = new Reservation();
  private Reservation reservation3 = new Reservation();

  @BeforeEach
  void setUp() {

    speciality.setId(1L);
    speciality.setName("Haircut");
    speciality.setPrice(37.29);
    speciality.setBeautyServiceId(0);
    
    employee.setId(1L);
    employee.setFullName("John Doe");
    employee.setEmail("john@plaza.pt");
    employee.setPhoneNumber("912345678");
    employee.setSpecialitiesID(List.of(speciality.getId()));

    reservation1.setId(1L);
    reservation1.setCustomerName("Josh Doe");
    reservation1.setCustomerEmail("josh@gmail.com");
    reservation1.setCustomerPhoneNumber("912345678");
    reservation1.setSecretCode("asjd8jkf");
    reservation1.setTimestamp(System.currentTimeMillis() + 129839);
    reservation1.setEmployee(employee);
    reservation1.setSpeciality(speciality);

    reservation2.setId(2L);
    reservation2.setCustomerName("Jane Johnson");
    reservation2.setCustomerEmail("jane@gmail.com");
    reservation2.setCustomerPhoneNumber("123456789");
    reservation2.setSecretCode("udnaiuhufn");
    reservation2.setTimestamp(System.currentTimeMillis() + 127343942);
    reservation2.setEmployee(employee);
    reservation2.setSpeciality(speciality);

    reservation3.setId(3L);
    reservation3.setCustomerName("Ulsa Smith");
    reservation3.setCustomerEmail("ulsa@gmail.com");
    reservation3.setCustomerPhoneNumber("287492173");
    reservation3.setSecretCode("wyqasncasqwd");
    reservation3.setTimestamp(System.currentTimeMillis() + 736392);
    reservation3.setSpeciality(speciality);

    employee.setReservations(List.of(reservation1, reservation2));

  }

  @Test
  @DisplayName("Search a reservation with success by its ID")
  void searchReservationWithSuccessByID() throws Exception {
    
    when(reservationService.getReservation(anyLong())).thenReturn(reservation1);

    mvc.perform(get("/api/reservation/" + reservation1.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.customerName", is("Josh Doe")))
      .andExpect(jsonPath("$.customerEmail", is("josh@gmail.com")))
      .andExpect(jsonPath("$.customerPhoneNumber", is("912345678")))
      .andExpect(jsonPath("$.secretCode", is("asjd8jkf")))
      .andExpect(jsonPath("$.timestamp", is(reservation1.getTimestamp())));

    verify(reservationService, times(1)).getReservation(anyLong());

  }

  @Test
  @DisplayName("Search a reservation that with an ID that doesn't exists")
  void searchReservationWithNonExistingID() throws Exception {
    
    when(reservationService.getReservation(anyLong())).thenThrow(new EntityNotFoundException("Reservation not found"));

    mvc.perform(get("/api/reservation/4"))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$").doesNotExist());

    verify(reservationService, times(1)).getReservation(anyLong());

  }

  @Test
  @DisplayName("Get all the reservations from an employee ID")
  void getAllReservationsFromEmployeeID() throws Exception {
    
    when(reservationService.getReservationsByEmployeeID(anyLong())).thenReturn(List.of(reservation1, reservation2));

    mvc.perform(get("/api/reservation/employee/" + employee.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(2)))
      .andExpect(jsonPath("$[0].customerName", is("Josh Doe")))
      .andExpect(jsonPath("$[1].customerName", is("Jane Johnson")));

    verify(reservationService, times(1)).getReservationsByEmployeeID(anyLong());

  }

  @Test
  @DisplayName("Get all the reservations from an customer email")
  void getAllReservationsFromCustomerEmail() throws Exception {
    
    when(reservationService.getReservationsByCustomerEmail(anyString())).thenReturn(List.of(reservation2));

    mvc.perform(get("/api/reservation/customer/" + reservation2.getCustomerEmail()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(1)))
      .andExpect(jsonPath("$[0].customerName", is("Jane Johnson")));

    verify(reservationService, times(1)).getReservationsByCustomerEmail(anyString());

  }

  @Test
  @DisplayName("Get a reservation by its secret code")
  void getReservationBySecretCode() throws Exception {
    
    when(reservationService.getReservationBySecretCode(anyString())).thenReturn(reservation3);

    mvc.perform(get("/api/reservation/code/" + reservation3.getSecretCode()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.customerName", is("Ulsa Smith")))
      .andExpect(jsonPath("$.customerEmail", is("ulsa@gmail.com")));

    verify(reservationService, times(1)).getReservationBySecretCode(anyString());

  }

  @Test
  @DisplayName("Fail to get a reservation by an invalid secret code")
  void failToGetReservationByInvalidSecretCode() throws Exception {
    
    when(reservationService.getReservationBySecretCode(anyString())).thenThrow(new EntityNotFoundException("Reservation not found"));

    mvc.perform(get("/api/reservation/code/invalid"))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$").doesNotExist());

    verify(reservationService, times(1)).getReservationBySecretCode(anyString());

  }

  @Test
  @DisplayName("Get all the reservations")
  void getAllReservations() throws Exception {
    
    when(reservationService.findAll()).thenReturn(List.of(reservation1, reservation2, reservation3));

    mvc.perform(get("/api/reservation/all"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(3)))
      .andExpect(jsonPath("$[0].customerName", is("Josh Doe")))
      .andExpect(jsonPath("$[1].customerName", is("Jane Johnson")))
      .andExpect(jsonPath("$[2].customerName", is("Ulsa Smith")));

    verify(reservationService, times(1)).findAll();

  }

}
