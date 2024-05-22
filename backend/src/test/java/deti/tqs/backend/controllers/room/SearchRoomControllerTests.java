package deti.tqs.backend.controllers.room;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
class SearchRoomControllerTests {
  
  private MockMvc mvc;

  @Autowired
  SearchRoomControllerTests(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private RoomService roomService;

  private Room r1 = new Room();
  private Room r2 = new Room();
  private Room r3 = new Room();

  private Facility f1 = new Facility();
  
  /*
   * NECESSARY TESTS
   * 
   *  1. Search for a room with an ID that exists
   *  2. Search for a room with an ID that does not exist
   *  3. Search for all rooms available
   *  4. Search for a room without a name an facility ID
   *  5. Search for a room with a facility ID
   *  6. Search for a room with a facility ID that does not exist
   *  7. Search for a room with a facility name
   *  8. Search for a room with a facility name that does not exist
   *  9. Search for a room with a name and facility ID
   * 10. Search for a room with a name or facility ID that does not exist
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

    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);
    r1.setFacility(f1);

    r2.setName("Room 2");
    r2.setMaxChairsCapacity(20);
    r2.setFacility(f1);

    r3.setName("Room 3");
    r3.setMaxChairsCapacity(30);
    r3.setFacility(f1);

  }

  @Test
  @DisplayName("Search for a room with an ID that exists")
  void searchRoomWithExistingID() throws Exception {

    when(roomService.findById(anyLong())).thenReturn(r1);

    mvc.perform(get("/api/room/1").contentType("application/json"))
      .andExpect(status().isFound())
      .andExpect(jsonPath("$.name", is("Room 1")))
      .andExpect(jsonPath("$.maxChairsCapacity", is(10)));

    verify(roomService, times(1)).findById(anyLong());

  }

  @Test
  @DisplayName("Search for a room with an ID that does not exist")
  void searchRoomWithNonExistingID() throws Exception {

    when(roomService.findById(anyLong())).thenThrow(new EntityNotFoundException("Room does not exist"));

    mvc.perform(get("/api/room/293").contentType("application/json"))
      .andExpect(status().isNotFound());

    verify(roomService, times(1)).findById(anyLong());

  }

  @Test
  @DisplayName("Search for all rooms available")
  void searchAllRooms() throws Exception {

    when(roomService.findAllRooms()).thenReturn(List.of(r1, r2, r3));

    mvc.perform(get("/api/room/all").contentType("application/json"))
      .andExpect(status().isFound())
      .andExpect(jsonPath("$[0].name", is("Room 1")))
      .andExpect(jsonPath("$[0].maxChairsCapacity", is(10)))
      .andExpect(jsonPath("$[1].name", is("Room 2")))
      .andExpect(jsonPath("$[1].maxChairsCapacity", is(20)))
      .andExpect(jsonPath("$[2].name", is("Room 3")))
      .andExpect(jsonPath("$[2].maxChairsCapacity", is(30)));

    verify(roomService, times(1)).findAllRooms();

  }

  @Test
  @DisplayName("Search for a room without a name an facility ID")
  void searchRoomWithoutNameAndFacilityID() throws Exception {

    when(roomService.searchByFacilityInfo(null, 0)).thenThrow(new NoSuchFieldException("At least one parameter must be provided"));

    mvc.perform(
      get("/api/room/search")
      .contentType("application/json")
      .param("facilityID", "0")
    )
      .andExpect(status().isBadRequest());

    verify(roomService, times(0)).searchByFacilityInfo(anyString(), anyLong());

  }

  @Test
  @DisplayName("Search for a room with a facility ID")
  void searchRoomByFacilityID() throws Exception {

    when(roomService.searchByFacilityInfo(null, 1)).thenReturn(List.of(r1, r2, r3));

    mvc.perform(
      get("/api/room/search")
      .contentType("application/json")
      .param("facilityID", "1")
    )
      .andExpect(status().isFound())
      .andExpect(jsonPath("$[0].name", is("Room 1")))
      .andExpect(jsonPath("$[0].maxChairsCapacity", is(10)))
      .andExpect(jsonPath("$[1].name", is("Room 2")))
      .andExpect(jsonPath("$[1].maxChairsCapacity", is(20)))
      .andExpect(jsonPath("$[2].name", is("Room 3")))
      .andExpect(jsonPath("$[2].maxChairsCapacity", is(30)));

    verify(roomService, times(1)).searchByFacilityInfo(eq(null), anyLong());

  }

  @Test
  @DisplayName("Search for a room with a facility ID that does not exist")
  void searchRoomByNonExistingFacilityID() throws Exception {

    when(roomService.searchByFacilityInfo(null, 1029L)).thenThrow(new EntityNotFoundException("Room does not exist"));

    mvc.perform(
      get("/api/room/search")
      .contentType("application/json")
      .param("facilityID", "1029")
    )
      .andExpect(status().isNotFound());

    verify(roomService, times(1)).searchByFacilityInfo(eq(null), anyLong());

  }

  @Test
  @DisplayName("Search for a room with a facility name")
  void searchRoomByFacilityName() throws Exception {

    when(roomService.searchByFacilityInfo("Facility 1", 0)).thenReturn(List.of(r1, r2, r3));

    mvc.perform(
      get("/api/room/search")
      .contentType("application/json")
      .param("facilityName", "Facility 1")
      .param("facilityID", "0")
    )
      .andExpect(status().isFound())
      .andExpect(jsonPath("$[0].name", is("Room 1")))
      .andExpect(jsonPath("$[0].maxChairsCapacity", is(10)))
      .andExpect(jsonPath("$[1].name", is("Room 2")))
      .andExpect(jsonPath("$[1].maxChairsCapacity", is(20)))
      .andExpect(jsonPath("$[2].name", is("Room 3")))
      .andExpect(jsonPath("$[2].maxChairsCapacity", is(30)));

    verify(roomService, times(1)).searchByFacilityInfo(anyString(), eq(0L));

  }

  @Test
  @DisplayName("Search for a room with a facility name that does not exist")
  void searchRoomByNonExistingFacilityName() throws Exception {

    when(roomService.searchByFacilityInfo("Facility 1029", 0)).thenThrow(new EntityNotFoundException("Room does not exist"));

    mvc.perform(
      get("/api/room/search")
      .contentType("application/json")
      .param("facilityName", "Facility 1029")
      .param("facilityID", "0")
    )
      .andExpect(status().isNotFound());

    verify(roomService, times(1)).searchByFacilityInfo(anyString(), eq(0L));

  }

  @Test
  @DisplayName("Search for a room with a name and facility ID")
  void searchRoomByNameAndFacilityID() throws Exception {

    when(roomService.searchByFacilityInfo("Room 1", 1)).thenReturn(List.of(r1));

    mvc.perform(
      get("/api/room/search")
      .contentType("application/json")
      .param("facilityName", "Room 1")
      .param("facilityID", "1")
    )
      .andExpect(status().isFound())
      .andExpect(jsonPath("$[0].name", is("Room 1")))
      .andExpect(jsonPath("$[0].maxChairsCapacity", is(10)));

    verify(roomService, times(1)).searchByFacilityInfo(anyString(), anyLong());

  }

  @Test
  @DisplayName("Search for a room with a name or facility ID that does not exist")
  void searchRoomByNameOrFacilityIDNotFound() throws Exception {

    when(roomService.searchByFacilityInfo("Room 1029", 1029L)).thenThrow(new EntityNotFoundException("Room does not exist"));

    mvc.perform(
      get("/api/room/search")
      .contentType("application/json")
      .param("facilityName", "Room 1029")
      .param("facilityID", "1029")
    )
      .andExpect(status().isNotFound());

    verify(roomService, times(1)).searchByFacilityInfo(anyString(), anyLong());

  }


}
