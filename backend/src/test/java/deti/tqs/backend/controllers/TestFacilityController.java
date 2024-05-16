package deti.tqs.backend.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import deti.tqs.backend.models.Facility;
import deti.tqs.backend.services.FacilityService;

@WebMvcTest(FacilityController.class)
@AutoConfigureMockMvc(addFilters = false) // avoid security filter
class TestFacilityController {

  private MockMvc mvc;

  @Autowired
  TestFacilityController(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private FacilityService facilityService;

  private Facility f1 = new Facility();
  private Facility f2 = new Facility();
  private Facility f3 = new Facility();

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
    f3.setStreetName("Rua da Batalha");
    f3.setPostalCode("3928-291");

    when(facilityService.save(any())).thenReturn(f1);

    when(facilityService.getFacilityById(f2.getId())).thenReturn(f2);
    when(facilityService.getFacilityById(412314L)).thenReturn(null);

  }

  @Test
  @DisplayName("Test create a facility with success")
  public void testCreateFacilityWithSuccess() throws Exception {
    
    mvc.perform(
      post("/api/facility/admin/add")
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"name\": \"Facility 1\"" + 
          "\"city\": \"Aveiro\"" +
          "\"streetName\": \"Rua de Aveiro\"" + 
          "\"postalCode\": \"3810-193\"" +
          "\"phoneNumber\": \"123456789\" }"
    ))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id", is(1)))
      .andExpect(jsonPath("$.name", is("Facility 1")));

    verify(facilityService, times(1)).save(any());

  }

  @Test
  @DisplayName("Test retrieve a facility with success by its id")
  public void testGetFacilityByIdWithSuccess() throws Exception {

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
  public void testGetNonExistingFacility() throws Exception {

    mvc.perform(get("/api/facility/412314").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$").doesNotExist());

    verify(facilityService, times(1)).getFacilityById(412314L);

  }

}