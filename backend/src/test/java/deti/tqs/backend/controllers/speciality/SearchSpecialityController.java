package deti.tqs.backend.controllers.speciality;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

import deti.tqs.backend.controllers.SpecialityController;
import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.services.SpecialityService;

@WebMvcTest(SpecialityController.class)
@AutoConfigureMockMvc(addFilters = false)
class SearchSpecialityControllerTests {
  
  private MockMvc mvc;

  @Autowired
  SearchSpecialityControllerTests(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private SpecialityService specialityService;

  /*
   * NECESSARY TESTS
   * 
   *  1. Test get specialities by beauty service id with success
   *
  */

  private Speciality haircut = new Speciality();
  private Speciality beardtrimming = new Speciality();

  @BeforeEach
  void setUp() {

    haircut.setName("Haircut");
    haircut.setPrice(29.21);
    haircut.setBeautyServiceId(1);

    beardtrimming.setName("Beard Trimming");
    beardtrimming.setPrice(15.0);
    beardtrimming.setBeautyServiceId(1);

  }

  @Test
  @DisplayName("Get specialities by beauty service id with success")
  void getSpecialitiesByBeautyServiceIdWithSuccess() throws Exception {

    when(specialityService.getSpecialityByBeautyServiceId(anyInt())).thenReturn(List.of(haircut, beardtrimming));

    mvc.perform(get("/api/speciality/1").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$[0].name").value("Haircut"))
      .andExpect(jsonPath("$[0].price").value(29.21))
      .andExpect(jsonPath("$[0].beautyServiceId").value(1))
      .andExpect(jsonPath("$[1].name").value("Beard Trimming"))
      .andExpect(jsonPath("$[1].price").value(15.0))
      .andExpect(jsonPath("$[1].beautyServiceId").value(1));

    verify(specialityService, times(1)).getSpecialityByBeautyServiceId(1);
  }

}
