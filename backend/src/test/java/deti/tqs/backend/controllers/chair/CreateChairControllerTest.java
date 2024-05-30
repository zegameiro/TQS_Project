package deti.tqs.backend.controllers.chair;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.backend.JsonUtils;
import deti.tqs.backend.controllers.ChairController;
import deti.tqs.backend.dtos.ChairSchema;
import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.services.ChairService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(ChairController.class)
@AutoConfigureMockMvc(addFilters = false)
class CreateChairControllerTest {
  
  private MockMvc mvc;

  @Autowired
  CreateChairControllerTest(MockMvc mvc) {
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

  @BeforeEach
  void setUp() {

    r1.setId(1L);
    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);

    c1.setId(1l);
    c1.setName("Chair 1");
    c1.setRoom(r1);

    cs1 = new ChairSchema(
      "Chair 1",
      1L
    );

  }

  @Test
  @DisplayName("Create a chair with success")
  void createChairWithSuccess() throws Exception {
    
    when(chairService.addChair(any(), anyLong())).thenReturn(c1);

    mvc.perform(
      post("/api/chair/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(cs1))
    )
    .andExpect(status().isCreated())
    .andExpect(jsonPath("$.name").value("Chair 1"));

    verify(chairService, times(1)).addChair(any(), anyLong());

  }

  @Test
  @DisplayName("Create a chair with a name that already exists in a room")
  void createChairWithExistingName() throws Exception {
    
    when(chairService.addChair(any(), anyLong())).thenThrow(new EntityExistsException("Chair with this name already exists in this room"));

    mvc.perform(
      post("/api/chair/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(cs1))
    )
    .andExpect(status().isConflict());

    verify(chairService, times(1)).addChair(any(), anyLong());

  }

  @Test
  @DisplayName("Create a chair with an invalid name")
  void createChairWithInvalidName() throws Exception {

    ChairSchema cs2 = new ChairSchema(
      "",
      1L
    );

    when(chairService.addChair(any(), anyLong())).thenThrow(new NoSuchFieldException("Chair must have a name"));

    mvc.perform(
      post("/api/chair/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(cs2))
    )
    .andExpect(status().isBadRequest());

    verify(chairService, times(1)).addChair(any(), anyLong());

  }

  @Test
  @DisplayName("Create a chair to a room that does not exists")
  void createChairToNonExistingRoom() throws Exception {

    ChairSchema cs2 = new ChairSchema(
      "Chair 1",
      1L
    );

    when(chairService.addChair(any(), anyLong())).thenThrow(new EntityNotFoundException("This room does not exists"));

    mvc.perform(
      post("/api/chair/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(cs2))
    )
    .andExpect(status().isNotFound());

    verify(chairService, times(1)).addChair(any(), anyLong());

  }

  @Test
  @DisplayName("Create a chair in a room that already has the maximum number of chairs filled")
  void createChairInFullRoom() throws Exception {

    ChairSchema cs2 = new ChairSchema(
      "Chair 1",
      1L
    );

    when(chairService.addChair(any(), anyLong())).thenThrow(new IllegalStateException("Room is at full capacity"));

    mvc.perform(
      post("/api/chair/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(cs2))
    )
    .andExpect(status().isUnprocessableEntity());

    verify(chairService, times(1)).addChair(any(), anyLong());

  }
  

}
