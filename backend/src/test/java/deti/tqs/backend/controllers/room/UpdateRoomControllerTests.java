package deti.tqs.backend.controllers.room;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import deti.tqs.backend.controllers.RoomController;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.services.RoomService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(RoomController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UpdateRoomControllerTests {

    private MockMvc mvc;

    @Autowired
    UpdateRoomControllerTests(MockMvc mvc) {
        this.mvc = mvc;
    }

    @MockBean
    private RoomService roomService;

    private Room r1 = new Room();

    /*
     * NECESSARY TESTS
     * 
     * 1. Update a room with success
     * 2. Update a room that does not exist
     * 
     */

    @BeforeEach
    void setUp() {
        r1.setId(1L);
        r1.setName("Room 1");
        r1.setMaxChairsCapacity(6);
    }

    @Test
    @DisplayName("Update a room with success")
    void testUpdateRoomWithSuccess() throws Exception {

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
    }

    @Test
    @DisplayName("Update a room that does not exist")
    void testUpdateNonExistingRoom() throws Exception {

        when(roomService.updateRoom(any(), anyLong(), anyLong()))
                .thenThrow(new EntityNotFoundException("Room does not exist"));

        mvc.perform(
                put("/api/room/admin/update?id=" + 1)
                        .contentType("application/json")
                        .content("{\"name\":\"Room 1 updated\",\"maxChairsCapacity\":7}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());

        verify(roomService, times(1)).updateRoom(any(), anyLong(), anyLong());
    }

}
