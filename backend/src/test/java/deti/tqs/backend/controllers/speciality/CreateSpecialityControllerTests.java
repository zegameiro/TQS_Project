package deti.tqs.backend.controllers.speciality;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.backend.controllers.SpecialityController;
import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.services.SpecialityService;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;

@WebMvcTest(SpecialityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CreateSpecialityControllerTests {
    
    private MockMvc mvc;

    @Autowired
    CreateSpecialityControllerTests(MockMvc mvc) {
        this.mvc = mvc;
    }

    @MockBean
    private SpecialityService specialityService;

    /*
     * NECESSARY TESTS
     * 
     * 1. Receive status 200 when creating a speciality with success
     */

    @BeforeEach
    public void setUp() {
        Speciality speciality = new Speciality();
        speciality.setName("Speciality");
        speciality.setPrice(10.0);
        speciality.setBeautyServiceId(1);
    }

    @Test
    @DisplayName("Create speciality with success")
    public void createSpecialityWithSuccess() throws Exception {
        // Arrange
        Speciality speciality = new Speciality();
        speciality.setName("Speciality");
        speciality.setPrice(10.0);
        speciality.setBeautyServiceId(1);

        String CONTENT = 
            "{\n" +
            "    \"name\": \"Speciality\",\n" +
            "    \"price\": 10.0,\n" +
            "    \"beautyServiceId\": 1\n" +
            "}";

        when(specialityService.save(speciality)).thenReturn(speciality);

        mvc.perform(
            post("/api/speciality/admin/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(CONTENT)
        )
        .andExpect(status().isCreated());

    }

}
