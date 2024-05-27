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
import deti.tqs.backend.dtos.ChairSchema;
import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Room;
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

  private ChairSchema cs1;
  private Chair c1 = new Chair();
  private Room r1 = new Room();

  /*
   * NECESSARY TESTS
   * 
   *  1. Create a chair with success
   *  2. Create a chair with a name that already exists in a room
   *  3. Create a chair with an invalid name
   *  4. Create a chair to a room that does not exists
   *  5. Create a chair in a room that already has the maximum number of chairs filled
   * 
  */


  @Test
  @DisplayName("Create a chair with success")
  void createChairWithSuccess() throws Exception {
    mvc.perform(
      post("/api/chair/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"Something\": \"Anything\"}")
    )
    .andExpect(status().isForbidden());

    verify(chairService, times(0)).addChair(any(), anyLong());

  }

  @Test
  @DisplayName("Create a chair with a name that already exists in a room")
  void createChairWithExistingName() throws Exception {
    
    mvc.perform(
      post("/api/chair/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"Something\": \"Anything\"}")
    )
    .andExpect(status().isForbidden());

    verify(chairService, times(0)).addChair(any(), anyLong());

  }

  @Test
  @DisplayName("Create a chair with an invalid name")
  void createChairWithInvalidName() throws Exception {

    mvc.perform(
      post("/api/chair/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"Something\": \"Anything\"}")
    )
    .andExpect(status().isForbidden());

    verify(chairService, times(0)).addChair(any(), anyLong());

  }

  @Test
  @DisplayName("Create a chair to a room that does not exists")
  void createChairToNonExistingRoom() throws Exception {

    mvc.perform(
      post("/api/chair/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"Something\": \"Anything\"}")
    )
    .andExpect(status().isForbidden());

    verify(chairService, times(0)).addChair(any(), anyLong());

  }

  @Test
  @DisplayName("Create a chair in a room that already has the maximum number of chairs filled")
  void createChairInFullRoom() throws Exception {
    
    mvc.perform(
      post("/api/chair/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"Something\": \"Anything\"}")
    )
    .andExpect(status().isForbidden());

    verify(chairService, times(0)).addChair(any(), anyLong());

  }
  

}
