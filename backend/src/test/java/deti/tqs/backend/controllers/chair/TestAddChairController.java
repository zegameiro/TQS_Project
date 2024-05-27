package deti.tqs.backend.controllers.chair;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;


import deti.tqs.backend.JsonUtils;
import deti.tqs.backend.controllers.ChairController;
import deti.tqs.backend.dtos.ChairSchema;
import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.services.ChairService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChairController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TestAddChairController {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ChairService chairService;

    private static Room room = new Room();

    /* NECESSARY TESTS */
    /*
    * 1. When post valid chair, create chair with success
    * 2. Save a chair with success without a room
    * 3. New chair is available by default

    */


    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("When post valid chair, create chair with success")
    void testWhenPostChairThenCreateChair() throws Exception {
        ChairSchema chairSchema = new ChairSchema(
            "Good chair", 
            true, 
            room
        );
        
        Chair chair = new Chair();
        chair.setName(chairSchema.name());
        chair.setAvailable(chairSchema.available());
        chair.setRoom(chairSchema.room());

        // String CONTENT = "{" +
        //     "\"name\": \"Good Chair\"," +
        //     "\"available\": \"true\"," +
        //     "\"room\": \"1L\"" +
        //     "}";


        when(chairService.addChair(any())).thenReturn(chair);

        mvc.perform(
            post("/api/chair/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(chair))
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is(chairSchema.name())))
            .andExpect(jsonPath("$.available", is(chairSchema.available())))
            .andExpect(jsonPath("$.room.id", is(((int) chairSchema.room().getId()))));
    }

    @Test
    @DisplayName("Save a chair with success without a room")
    void testWhenPostChairWithoutRoom_thenCreateChair() throws Exception {
        ChairSchema chairSchema = new ChairSchema(
            "Good Chair",
            true,
            null
        );

        Chair chair = new Chair();
        chair.setName(chairSchema.name());
        chair.setAvailable(chairSchema.available());
        chair.setRoom(chairSchema.room());


        when(chairService.addChair(any())).thenReturn(chair);

        mvc.perform(
            post("/api/chair/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(chair))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is(chairSchema.name())))
        .andExpect(jsonPath("$.available", is(chairSchema.available())));
    }

}
