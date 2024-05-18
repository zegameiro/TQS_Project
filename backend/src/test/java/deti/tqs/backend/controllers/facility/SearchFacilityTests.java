package deti.tqs.backend.controllers.facility;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

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

import deti.tqs.backend.controllers.FacilityController;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.services.FacilityService;

@WebMvcTest(FacilityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SearchFacilityTests {

  private MockMvc mvc;

  @Autowired
  SearchFacilityTests(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private FacilityService facilityService;

  private Facility f1 = new Facility();
  private Facility f2 = new Facility();
  private Facility f3 = new Facility();
  private Facility f4 = new Facility();

  /*
   * NECESSARY TESTS
   * 
   *  1. Retrieve a facility with success by its id
   *  2. Retrieve a facility that does not exist
   *  3. Retrieve all facilities
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

    f2.setId(2L);
    f2.setName("Facility 2");
    f2.setCity("Porto");
    f2.setPhoneNumber("987654321");
    f2.setPostalCode("4000-007");
    f2.setStreetName("Rua do Porto");

    f3.setId(3L);
    f3.setName("Facility 3");
    f3.setCity("Leiria");
    f3.setPhoneNumber("987654321");
    f3.setStreetName("Rua da Batalha");
    f3.setPostalCode("3928-291");

    f4.setId(4L);
    f4.setName("Facility 4");
    f4.setCity("Lisboa");
    f4.setPhoneNumber("982641741");
    f4.setStreetName("Rua de Lisboa");
    f4.setPostalCode("1000-001");

    when(facilityService.getFacilityById(f2.getId())).thenReturn(f2);
    when(facilityService.getFacilityById(412314L)).thenReturn(null);

    when(facilityService.getAllFacilities()).thenReturn(List.of(f1, f2, f3, f4));

  }

  @Test
  @DisplayName("Test retrieve a facility with success by its id")
  void testGetFacilityByIdWithSuccess() throws Exception {

    mvc.perform(get("/api/facility/" + f2.getId()).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name", is("Facility 2")))
      .andExpect(jsonPath("$.city", is("Porto")))
      .andExpect(jsonPath("$.phoneNumber", is("987654321")))
      .andExpect(jsonPath("$.postalCode", is("4000-007")))
      .andExpect(jsonPath("$.streetName", is("Rua do Porto")));

    verify(facilityService, times(1)).getFacilityById(f2.getId());

  }

  @Test
  @DisplayName("Test retrieve a facility that does not exist")
  void testGetNonExistingFacility() throws Exception {

    mvc.perform(get("/api/facility/412314").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$").doesNotExist());

    verify(facilityService, times(1)).getFacilityById(412314L);

  }

  @Test
  @DisplayName("Test retrieve all facilities")
  void testGetAllAvailableFacilities() throws Exception {
      
    mvc.perform(get("/api/facility/all").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(4)))
      .andExpect(jsonPath("$[0].name", is("Facility 1")))
      .andExpect(jsonPath("$[1].name", is("Facility 2")))
      .andExpect(jsonPath("$[2].name", is("Facility 3")))
      .andExpect(jsonPath("$[3].name", is("Facility 4")));

    verify(facilityService, times(1)).getAllFacilities();

  }
  
}
