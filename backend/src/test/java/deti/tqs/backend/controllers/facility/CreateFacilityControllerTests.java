package deti.tqs.backend.controllers.facility;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import deti.tqs.backend.controllers.FacilityController;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.services.FacilityService;
import jakarta.persistence.EntityExistsException;

@WebMvcTest(FacilityController.class)
@AutoConfigureMockMvc(addFilters = false)
class CreateFacilityControllerTests {
  
  private MockMvc mvc;

  @Autowired
  CreateFacilityControllerTests(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private FacilityService facilityService;

  private Facility f1 = new Facility();

  /*
   * NECESSARY TESTS 
   * 
   *  1. Create a facility with success
   *  2. create a facility with missing fields
   *  3. Create a facility with a name that already exists
   *  4. Create a facility with an invalid capacity
   * 
  */

  @BeforeEach
  void setUp() {
    
    f1.setId(1L);
    f1.setName("Facility 1");
    f1.setCity("Aveiro");
    f1.setPhoneNumber("123456789");
    f1.setPostalCode("3810-193");
    f1.setStreetName("Rua de Aveiro");

  }

  @Test
  @DisplayName("Test create a facility with success")
  void testCreateFacilityWithSuccess() throws Exception {
    
    when(facilityService.save(any())).thenReturn(f1);

    mvc.perform(
      post("/api/facility/admin/add")
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"name\": \"Facility 1\"," + 
          "\"city\": \"Aveiro\"," +
          "\"streetName\": \"Rua de Aveiro\"," + 
          "\"postalCode\": \"3810-193\"," +
          "\"phoneNumber\": \"123456789\" }"
    ))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id", is(1)))
      .andExpect(jsonPath("$.name", is("Facility 1")));

    verify(facilityService, times(1)).save(any());

  }

  @Test
  @DisplayName("Test create a facility with missing fields")
  void testCrateFacilityWithMissingFields() throws Exception {

    when(facilityService.save(any())).thenThrow(NoSuchFieldException.class);

    mvc.perform(
      post("/api/facility/admin/add")
      .contentType(MediaType.APPLICATION_JSON)
      .content("{ \"name\": \"Facility 4\"," + 
        "\"city\": \"Aveiro\"," +
        "\"streetName\": \"Rua de Aveiro\"," + 
        "\"postalCode\": \"3810-193\"}"
    ))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$").doesNotExist());

    verify(facilityService, times(0)).save(any());

  }

  @Test
  @DisplayName("Test create a facility with a name that already exists")
  void testCreateFacilityWithExistingName() throws Exception {

    when(facilityService.save(any())).thenThrow(new EntityExistsException("Facility with this name already exists"));

    MvcResult res = mvc.perform(
      post("/api/facility/admin/add")
      .contentType(MediaType.APPLICATION_JSON)
      .content(
        "{\"name\": \"Facility 2\"," + 
        "\"city\": \"Porto\"," +
        "\"streetName\": \"Rua do Porto\"," + 
        "\"postalCode\": \"4000-007\"," +
        "\"phoneNumber\": \"987654321\" }"
      )
    )
    .andExpect(status().isConflict())
    .andReturn();

    assertNull(res.getModelAndView()); // Check if the response is empty

    verify(facilityService, times(1)).save(any());

  }

  @Test
  @DisplayName("Test create a facility with an invalid capacity")
  void testCreateFacilityWithInvalidCapacity() throws Exception {

    when(facilityService.save(any())).thenThrow(new IllegalArgumentException("Facility must have a valid capacity digit greater than 0"));

    mvc.perform(
      post("/api/facility/admin/add")
      .contentType(MediaType.APPLICATION_JSON)
      .content(
        "{\"name\": \"Facility 3\"," + 
        "\"city\": \"Lisboa\"," +
        "\"streetName\": \"Rua de Lisboa\"," + 
        "\"postalCode\": \"1938-092\"," +
        "\"phoneNumber\": \"823741291\"," +
        "\"maxRoomsCapacity\": 0 }"
      )
    )
    .andExpect(status().isBadRequest())
    .andExpect(jsonPath("$").doesNotExist());

    verify(facilityService, times(1)).save(any());

  }

}
