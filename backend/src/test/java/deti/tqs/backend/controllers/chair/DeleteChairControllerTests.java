package deti.tqs.backend.controllers.chair;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.backend.controllers.ChairController;
import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.services.ChairService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(ChairController.class)
@AutoConfigureMockMvc(addFilters = false)
class DeleteChairControllerTests {
  
  private MockMvc mvc;

  @Autowired
  DeleteChairControllerTests(MockMvc mvc) {
    this.mvc = mvc;
  }

  @MockBean
  private ChairService chairService;

  private Chair c1 = new Chair();
  private Chair c2 = new Chair();
  private Chair c3 = new Chair();

  private Room r1 = new Room();

  /*
   * NECESSARY TESTS
   * 
   *  1. Delete a chair with success that exists
   *  2. Delete a chair that does not exists 
   * 
  */

  @BeforeEach
  void setUp() {

    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);

    c1.setName("Chair 1");
    c1.setRoom(r1);

    c2.setName("Chair 2");
    c2.setRoom(r1);

    c3.setName("Chair 3");
    c3.setRoom(r1);

  }

  @Test
  @DisplayName("Delete a chair with success that exists")
  void deleteChairWithSuccess() throws Exception {

    doNothing().when(chairService).deleteChair(c1.getId());
    when(chairService.getAllChairs()).thenReturn(List.of(c2, c3));

    mvc.perform(
      delete("/api/chair/admin/delete?id=" + c1.getId())
    )
    .andExpect(status().isOk());

    mvc.perform(
      get("/api/chair/all")
    )
    .andExpect(status().isOk())
    .andExpect(jsonPath("$[0].name").value("Chair 2"))
    .andExpect(jsonPath("$[1].name").value("Chair 3"));

    verify(chairService, times(1)).deleteChair(anyLong());
    verify(chairService, times(1)).getAllChairs();

  }

  @Test
  @DisplayName("Delete a chair that does not exists")
  void deleteChairThatDoesNotExists() throws Exception {

    doThrow(new EntityNotFoundException("Chair not found")).when(chairService).deleteChair(anyLong());
    when(chairService.getAllChairs()).thenReturn(List.of(c1, c2, c3));

    mvc.perform(
      delete("/api/chair/admin/delete?id=12938")
    )
    .andExpect(status().isNotFound());

    mvc.perform(
      get("/api/chair/all")
    )
    .andExpect(status().isOk())
    .andExpect(jsonPath("$[0].name").value("Chair 1"))
    .andExpect(jsonPath("$[1].name").value("Chair 2"))
    .andExpect(jsonPath("$[2].name").value("Chair 3"));

    verify(chairService, times(1)).deleteChair(anyLong());
    verify(chairService, times(1)).getAllChairs();

  }

}
