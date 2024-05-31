package deti.tqs.backend.controllers.room;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.backend.controllers.RoomController;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.services.RoomService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(RoomController.class)
@AutoConfigureMockMvc(addFilters = false)
class DeleteRoomControllerTests {
  
  private MockMvc mvc;

  @Autowired
  DeleteRoomControllerTests(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private RoomService roomService;

  private Room r1 = new Room();
  private Facility f1 = new Facility();

  /*
   * NECESSARY TESTS
   * 
   *  1. Delete a room with success
   *  2. Delete a room that does not exist
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

    r1.setId(1L);
    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);
    r1.setFacility(f1);
    r1.setBeautyServiceId(4);

  }

  @Test
  @DisplayName("Delete a room with success")
  void testDeleteRoomWithSuccess() throws Exception {
    
    doNothing().when(roomService).deleteRoom(anyLong());

    mvc.perform(delete("/api/room/admin/delete?id=" + r1.getId()))
      .andExpect(status().isOk());

    verify(roomService, times(1)).deleteRoom(r1.getId());

  }

  @Test
  @DisplayName("Delete a room that does not exist")
  void testDeleteRoomThatDoesNotExist() throws Exception {
    
    doThrow(new EntityNotFoundException("Room does not exist")).when(roomService).deleteRoom(anyLong());

    mvc.perform(delete("/api/room/admin/delete?id=" + r1.getId()))
      .andExpect(status().isNotFound());

    verify(roomService, times(1)).deleteRoom(r1.getId());

  }



}
