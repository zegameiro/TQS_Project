package deti.tqs.backend.controllers.facility;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.backend.controllers.FacilityController;
import deti.tqs.backend.services.FacilityService;

@WebMvcTest(FacilityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DeleteFacilityControllerTests {

  private MockMvc mvc;

  @Autowired
  DeleteFacilityControllerTests(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private FacilityService facilityService;

  /*
   * NECESSARY TESTS
   * 
   * 1. Delete a facility with success
   * 2. Delete a facility that does not exist
   * 
   */

  @Test
  @DisplayName("Test delete a facility with success")
  void testDeleteFacilityWithSuccess() throws Exception {

    long exampleId = 1L;
    doNothing().when(facilityService).delete(anyLong());

    mvc.perform(delete("/api/facility/admin/delete?id=" + exampleId))
      .andExpect(status().isOk());

    verify(facilityService).delete(anyLong());
  
  }

  @Test
  @DisplayName("Test delete a facility that does not exist")
  void testDeleteNonExistingFacility() throws Exception {

    doThrow(new IllegalArgumentException("Facility not found")).when(facilityService).delete(anyLong());

    mvc.perform(delete("/api/facility/admin/delete?id=412314"))
      .andExpect(status().isNotFound());

    verify(facilityService).delete(anyLong());

  }
  
}
