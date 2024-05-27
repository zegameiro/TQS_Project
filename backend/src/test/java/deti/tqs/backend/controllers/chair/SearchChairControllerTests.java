package deti.tqs.backend.controllers.chair;

import static org.mockito.ArgumentMatchers.anyLong;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.backend.controllers.ChairController;
import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.services.ChairService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(ChairController.class)
@AutoConfigureMockMvc(addFilters = false)
class SearchChairControllerTests {
  
  private MockMvc mvc;

  @Autowired
  SearchChairControllerTests(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private ChairService chairService;

  private Chair c1 = new Chair();
  private Chair c2 = new Chair();
  private Chair c3 = new Chair();
  private Chair c4 = new Chair();
  private Chair c5 = new Chair();

  private Room r1 = new Room();
  private Room r2 = new Room();

  /*
   * NECESSARY TESTS
   * 
   *  1. Search for a chair that exists
   *  2. Search for a chair that does not exists
   *  3. Search for all the chairs that exists
   *  4. Search for all the chairs in a Room
   * 
  */

  @BeforeEach
  void setUp() {

    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);

    r2.setName("Room 2");
    r2.setMaxChairsCapacity(10);

    c1.setName("Chair 1");
    c1.setRoom(r1);

    c2.setName("Chair 2");
    c2.setRoom(r1);

    c3.setName("Chair 3");
    c3.setRoom(r1);

    c4.setName("Chair 4");
    c4.setRoom(r1);

    c5.setName("Chair 5");
    c5.setRoom(r2);

  }

  @Test
  @DisplayName("Search for a chair that exists")
  void searchForExistingChair() throws Exception {
    
    when(chairService.getChair(anyLong())).thenReturn(c1);

    mvc.perform(
      get("/api/chair/1")
      .contentType(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.name").value("Chair 1"));

    verify(chairService, times(1)).getChair(anyLong());

  }

  @Test
  @DisplayName("Search for a chair that does not exists")
  void searchForNonExistingChair() throws Exception {
    
    when(chairService.getChair(anyLong())).thenThrow(new EntityNotFoundException("Chair not found"));

    mvc.perform(
      get("/api/chair/1")
      .contentType(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isNotFound());

    verify(chairService, times(1)).getChair(anyLong());

  }

  @Test
  @DisplayName("Search for all the chairs that exists")
  void searchForAllChairs() throws Exception {
    
    when(chairService.getAllChairs()).thenReturn(List.of(c1, c2, c3, c4, c5));

    mvc.perform(
      get("/api/chair/all")
      .contentType(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isOk())
    .andExpect(jsonPath("$[0].name").value("Chair 1"))
    .andExpect(jsonPath("$[1].name").value("Chair 2"))
    .andExpect(jsonPath("$[2].name").value("Chair 3"))
    .andExpect(jsonPath("$[3].name").value("Chair 4"))
    .andExpect(jsonPath("$[4].name").value("Chair 5"));

    verify(chairService, times(1)).getAllChairs();

  }

  @Test
  @DisplayName("Search for all the chairs in a Room")
  void searchForAllChairsInRoom() throws Exception {
    
    when(chairService.getChairsByRoomID(anyLong())).thenReturn(List.of(c1, c2, c3, c4));

    mvc.perform(
      get("/api/chair/all?roomID=1")
      .contentType(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isOk())
    .andExpect(jsonPath("$[0].name").value("Chair 1"))
    .andExpect(jsonPath("$[1].name").value("Chair 2"))
    .andExpect(jsonPath("$[2].name").value("Chair 3"))
    .andExpect(jsonPath("$[3].name").value("Chair 4"));

    verify(chairService, times(1)).getChairsByRoomID(anyLong());

  }

}
