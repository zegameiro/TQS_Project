package deti.tqs.backend.controllers.chair;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.backend.controllers.ChairController;
import deti.tqs.backend.services.ChairService;

@WebMvcTest(ChairController.class)
@AutoConfigureMockMvc(addFilters = true)
class CreateChairControllerNoRightsTest {
  
  private MockMvc mvc;

  @Autowired
  CreateChairControllerNoRightsTest(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private ChairService chairService;

  /*
   * NECESSARY TESTS
   * 
   *  1. Fail to create a chair without admin rights
   * 
  */


  @Test
  @DisplayName("Create a chair with success")
  void failtToCreateChairWithoutAdminRights() throws Exception {
    mvc.perform(
      post("/api/chair/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"Something\": \"Anything\"}")
    )
    .andExpect(status().isForbidden());

    verify(chairService, times(0)).addChair(any(), anyLong());

  }  

}
