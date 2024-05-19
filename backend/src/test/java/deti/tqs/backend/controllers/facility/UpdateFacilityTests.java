package deti.tqs.backend.controllers.facility;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import deti.tqs.backend.controllers.FacilityController;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.services.FacilityService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(FacilityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UpdateFacilityTests {

  private MockMvc mvc;

  @Autowired
  UpdateFacilityTests(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private FacilityService facilityService;

  private Facility f = new Facility();

  /*
   * NECESSARY TESTS
   * 
   *  1. Update a facility with success
   *  2. Update a facility that does not exist
   * 
   */

  @BeforeEach
  void setUp() {

    f.setId(4L);
    f.setName("Facility 4");
    f.setCity("Lisboa");
    f.setPhoneNumber("982641741");
    f.setStreetName("Rua de Lisboa");
    f.setPostalCode("1000-001");

  }

  @Test
  @DisplayName("Test update a facility with success")
  void testUpdateFacilityWithSuccess() throws Exception {

    f.setId(2);

    when(facilityService.update(any(), anyLong())).thenReturn(f);

    mvc.perform(
      put("/api/facility/admin/update?id=" + 2)
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"name\": \"Facility 4\"," + 
        "\"city\": \"Lisboa\"," +
        "\"streetName\": \"Rua de Lisboa\"," + 
        "\"postalCode\": \"1000-001\"," +
        "\"phoneNumber\": \"982641741\" }"
    ))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(2)))
      .andExpect(jsonPath("$.name", is("Facility 4")));

    verify(facilityService, times(1)).update(any(), anyLong());

  }

  @Test
  @DisplayName("Test update a facility that does not exist")
  void testUpdateNonExistingFacility() throws Exception {

    when(facilityService.update(any(), anyLong())).thenThrow(new EntityNotFoundException("Facility not found"));

    mvc.perform(
      put("/api/facility/admin/update?id=412314")
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"name\": \"Facility 4\"," + 
        "\"city\": \"Lisboa\"," +
        "\"streetName\": \"Rua de Lisboa\"," + 
        "\"postalCode\": \"1000-001\"," +
        "\"phoneNumber\": \"982641741\" }"
    ))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$").doesNotExist());

    verify(facilityService, times(1)).update(any(), anyLong());

  }
  
}
