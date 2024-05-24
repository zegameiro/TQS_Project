package deti.tqs.backend.controllers.room;

import static org.hamcrest.CoreMatchers.is;
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
import deti.tqs.backend.controllers.RoomController;
import deti.tqs.backend.dtos.RoomSchema;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.services.RoomService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(RoomController.class)
@AutoConfigureMockMvc(addFilters = false)
class CreateRoomControllerTests {
  
  private MockMvc mvc;

  @Autowired
  CreateRoomControllerTests(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private RoomService roomService;

  private RoomSchema rs1;
  private Room r1 = new Room();
  private Facility f1 = new Facility();
  
  /*
   * NECESSARY TESTS
   * 
   *  1. Create a room with success
   *  2. Create a room with a name that already exists in a facility
   *  3. Create a room without a name
   *  4. Create a room with an invalid capacity
   *  5. Create a room to a facility that does not exists
   *  6. Create a room in a facility that already has the maximum number of rooms filled
   * 
  */

  @BeforeEach
  void setUp() {

    f1.setId(1L);
    f1.setName("Facility 1");
    f1.setCity("Aveiro");
    f1.setMaxRoomsCapacity(10);
    f1.setPhoneNumber("123456789");
    f1.setPostalCode("3810-193");
    f1.setStreetName("Rua de Aveiro");

    rs1 = new RoomSchema(
      "Room 1",
      10,
      1L 
    );

    r1.setId(1L);
    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);
  }

  @Test
  @DisplayName("Create a room with success")
  void createRoomWithSuccess() throws Exception {

    when(roomService.save(any(), anyLong())).thenReturn(r1);

    mvc.perform(
      post("/api/room/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(rs1))
    )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id", is(1)))
      .andExpect(jsonPath("$.name", is("Room 1"))) 
      .andExpect(jsonPath("$.maxChairsCapacity", is(10)));

    verify(roomService, times(1)).save(any(), anyLong());

  }

  @Test
  @DisplayName("Create a room with a name that already exists in a facility")
  void createRoomWithExistingName() throws Exception {
    
    when(roomService.save(any(), anyLong())).thenThrow(new EntityExistsException("Room with this name already exists in this facility"));

    mvc.perform(
      post("/api/room/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(rs1))
    )
      .andExpect(status().isConflict());

    verify(roomService, times(1)).save(any(), anyLong());

  }

  @Test
  @DisplayName("Create a room without a name")
  void createRoomWithoutName() throws Exception {
    
    when(roomService.save(any(), anyLong())).thenThrow(new NoSuchFieldException("Room must have a name"));

    mvc.perform(
      post("/api/room/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(new RoomSchema(null, 10, 1L))
    ))
      .andExpect(status().isBadRequest());

    verify(roomService, times(1)).save(any(), anyLong());

  }

  @Test
  @DisplayName("Create a room with an invalid capacity")
  void createRoomWithInvalidCapacity() throws Exception {
    
    when(roomService.save(any(), anyLong())).thenThrow(new NoSuchFieldException("Room must have a valid capacity value greater than 0"));

    mvc.perform(
      post("/api/room/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(new RoomSchema("Room 1", 0, 1L))
    ))
      .andExpect(status().isBadRequest());

    verify(roomService, times(1)).save(any(), anyLong());

  }

  @Test
  @DisplayName("Create a room to a facility that does not exists")
  void createRoomToNonExistingFacility() throws Exception {
    
    when(roomService.save(any(), anyLong())).thenThrow(new EntityNotFoundException("Facility does not exist"));

    mvc.perform(
      post("/api/room/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(rs1)
    ))
      .andExpect(status().isNotFound());

    verify(roomService, times(1)).save(any(), anyLong());

  }

  @Test
  @DisplayName("Create a room in a facility that already has the maximum number of rooms filled")
  void createRoomInFullFacility() throws Exception {
    
    when(roomService.save(any(), anyLong())).thenThrow(new IllegalStateException("Facility is at full capacity"));

    mvc.perform(
      post("/api/room/admin/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(rs1)
    ))
      .andExpect(status().isUnprocessableEntity());

    verify(roomService, times(1)).save(any(), anyLong());

  }
  
}
