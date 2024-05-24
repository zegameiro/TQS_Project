package deti.tqs.backend.controllers.room;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
class UpdateRoomControllerTests {
  
  private MockMvc mvc;

  @Autowired
  UpdateRoomControllerTests(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private RoomService roomService;

  private Facility f1 = new Facility();
  private Facility f2 = new Facility();

  private Room r1 = new Room();
  private Room r2 = new Room();

  /*
   * NECESSARY TESTS
   * 
   *  1. Test update a room with success
   *  2. Test update a room that does not exist
   *  3. Test update a room with a name that already exists in the facility
   *  4. Test update a room with an invalid name
   *  5. Test update a room with an invalid capacity
   *  6. Test update a room with a facility that does not exist
   *  7. Test update a room with a facility at full capacity
   *
   */

  @BeforeEach
  void setUp() {

    f1.setName("Facility 1");
    f1.setCity("Aveiro");
    f1.setMaxRoomsCapacity(1);
    f1.setPhoneNumber("123456789");
    f1.setStreetName("Street 1");
    f1.setPostalCode("1234-567");

    f2.setName("Facility 2");
    f2.setCity("Porto");
    f2.setMaxRoomsCapacity(20);
    f2.setPhoneNumber("987654321");
    f2.setStreetName("Street 2");
    f2.setPostalCode("9876-543");

    r1.setId(1L);
    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);
    r1.setFacility(f1);

    r2.setId(2L);
    r2.setName("Room 2");
    r2.setMaxChairsCapacity(20);
    r2.setFacility(f2);

  }

  @Test
  @DisplayName("Test update a room with success")
  void testUpdateRoomSuccess() throws Exception {

    r1.setName("Room 1 updated");
    r1.setMaxChairsCapacity(7);

    when(roomService.updateRoom(any(), anyLong(), anyLong())).thenReturn(r1);

    mvc.perform(
      put("/api/room/admin/update?id=" + 1)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\":\"Room 1 updated\",\"maxChairsCapacity\":7}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(1)))
      .andExpect(jsonPath("$.name", is("Room 1 updated")))
      .andExpect(jsonPath("$.maxChairsCapacity", is(7)));

    verify(roomService, times(1)).updateRoom(any(), anyLong(), anyLong());
  }

  @Test
  @DisplayName("Test update a room that does not exist")
  void testUpdateRoomDoesNotExist() throws Exception {

    RoomSchema roomSchema = new RoomSchema(
      "Room 1 updated",
      7,
      1
    );

    when(roomService.updateRoom(any(), anyLong(), anyLong())).thenThrow (new EntityNotFoundException("Room does not exist"));

    mvc.perform(
      put("/api/room/admin/update?id=" + 19283)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(roomSchema))
      )
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$").doesNotExist());

    verify(roomService, times(1)).updateRoom(any(), anyLong(), anyLong());

  }

  @Test
  @DisplayName("Test update a room with a name that already exists in the facility")
  void testUpdateRoomNameAlreadyExists() throws Exception {

    RoomSchema roomSchema = new RoomSchema(
      "Room 2",
      7,
      1
    );

    when(roomService.updateRoom(any(), anyLong(), anyLong())).thenThrow (new EntityExistsException("Room with this name already exists in this facility"));

    mvc.perform(
      put("/api/room/admin/update?id=" + 2)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(roomSchema))
      )
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$").doesNotExist());

    verify(roomService, times(1)).updateRoom(any(), anyLong(), anyLong());

  }

  @Test
  @DisplayName("Test update a room with an invalid name")
  void testUpdateRoomInvalidName() throws Exception {

    RoomSchema roomSchema = new RoomSchema(
      "",
      7,
      1
    );

    when(roomService.updateRoom(any(), anyLong(), anyLong())).thenThrow (new NoSuchFieldException("Room must have a name"));

    mvc.perform(
      put("/api/room/admin/update?id=" + 2)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(roomSchema))
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$").doesNotExist());

    verify(roomService, times(1)).updateRoom(any(), anyLong(), anyLong());

  }

  @Test
  @DisplayName("Test update a room with an invalid capacity")
  void testUpdateRoomInvalidCapacity() throws Exception {

    RoomSchema roomSchema = new RoomSchema(
      "Room 2",
      0,
      1
    );

    when(roomService.updateRoom(any(), anyLong(), anyLong())).thenThrow (new NoSuchFieldException("Room must have a valid capacity value greater than 0"));

    mvc.perform(
      put("/api/room/admin/update?id=" + 2)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(roomSchema))
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$").doesNotExist());

    verify(roomService, times(1)).updateRoom(any(), anyLong(), anyLong());

  }

  @Test
  @DisplayName("Test update a room with a facility that does not exist")
  void testUpdateRoomFacilityDoesNotExist() throws Exception {

    RoomSchema roomSchema = new RoomSchema(
      "Room 2",
      7,
      19283
    );

    when(roomService.updateRoom(any(), anyLong(), anyLong())).thenThrow (new EntityNotFoundException("Facility does not exist"));

    mvc.perform(
      put("/api/room/admin/update?id=" + 2)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(roomSchema))
      )
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$").doesNotExist());

    verify(roomService, times(1)).updateRoom(any(), anyLong(), anyLong());

  }

  @Test
  @DisplayName("Test update a room with a facility at full capacity")
  void testUpdateRoomFacilityFullCapacity() throws Exception {

    RoomSchema roomSchema = new RoomSchema(
      "Room 2",
      7,
      1
    );

    when(roomService.updateRoom(any(), anyLong(), anyLong())).thenThrow (new IllegalStateException("Facility is at full capacity"));

    mvc.perform(
      put("/api/room/admin/update?id=" + 2)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(roomSchema))
      )
      .andExpect(status().isUnprocessableEntity())
      .andExpect(jsonPath("$").doesNotExist());

    verify(roomService, times(1)).updateRoom(any(), anyLong(), anyLong());

  }

}
