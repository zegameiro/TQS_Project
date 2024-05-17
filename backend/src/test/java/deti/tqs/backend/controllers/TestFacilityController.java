package deti.tqs.backend.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import org.springframework.web.server.ResponseStatusException;

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
  private Facility f4 = new Facility();

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

    when(facilityService.save(any())).thenThrow(ResponseStatusException.class);

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

    when(facilityService.save(any())).thenThrow(new IllegalArgumentException("Facility with this name already exists"));

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

    assertNull(res.getModelAndView());

    verify(facilityService, times(1)).save(any());
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

  @Test
  @DisplayName("Test update a facility with success")
  void testUpdateFacilityWithSuccess() throws Exception {

    f4.setId(2);

    when(facilityService.update(any(), anyLong())).thenReturn(f4);

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

    when(facilityService.update(any(), anyLong())).thenThrow(new IllegalArgumentException("Facility not found"));

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