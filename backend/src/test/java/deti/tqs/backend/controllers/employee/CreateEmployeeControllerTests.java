package deti.tqs.backend.controllers.employee;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
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
import deti.tqs.backend.controllers.EmployeeController;
import deti.tqs.backend.models.Employee;
import deti.tqs.backend.services.EmployeeService;

import java.lang.NoSuchFieldException;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CreateEmployeeControllerTests {
    
    private MockMvc mvc;

    @Autowired
    CreateEmployeeControllerTests(MockMvc mvc) {
        this.mvc = mvc;
    }

    @MockBean
    private EmployeeService employeeService;

    private Employee employee = new Employee();

    /*
     * NECESSARY TESTS
     * 
     * 1. Receive status code isCreated when Saving an employee with success
     * 2. Receive status code badRequest when saving an employee with null fullName
     * 3. Receive status code badRequest when saving an employee with empty fullName
     * 4. Receive status code badRequest when saving an employee with null email
     * 5. Receive status code badRequest when saving an employee with empty email
     * 6. Receive status code badRequest when saving an employee with null phoneNumber
     * 7. Receive status code badRequest when saving an employee with empty phoneNumber
     * 8. Receive status code badRequest when saving an employee with existing email
     * 9. Receive status code badRequest when saving an employee with existing phoneNumber
     * 10. Receive status code badRequest when saving an employee with email not containing '@'
     * 11. Receive status code badRequest when saving an employee with email not containing '.'
     * 12. Receive status code badRequest when saving an employee with email containing '@' and '.', but not in the right order
     * 13. Receive status code badRequest when saving an employee with email containing more then one '@'
     * 14. Receive status code badRequest when saving an employee with email containing only '@.'
     * 13. Receive status code badRequest when saving an employee with a phoneNumber not containing only numbers
     */

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setAdmin(false);
        employee.setFullName("Employee 1");
        employee.setEmail("johndoe@gmail.com");
        employee.setPhoneNumber("123456789");
        employee.setSpecialitiesID(null);

    }

    @Test
    @DisplayName("Test save an employee with success")
    void whenSaveEmployee_thenCreateEmployee() throws Exception {

        String CONTENT = 
            "{" +
                "\"isAdmin\": \"false\"," +
                "\"fullName\": \"Employee 1\"," +
                "\"email\": \"johndoe@gmail.com\"," +
                "\"phoneNumber\": \"123456789\"," +
                "\"specialitiesID\": [1]" +
            "}";

        when(employeeService.save(any())).thenReturn(employee);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(CONTENT)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is((int) employee.getId())))
        .andExpect(jsonPath("$.fullName", is(employee.getFullName())))
        .andExpect(jsonPath("$.email", is(employee.getEmail())))
        .andExpect(jsonPath("$.phoneNumber", is(employee.getPhoneNumber())));
    }

    @Test
    @DisplayName("Test save an employee with null fullName")
    void whenSaveEmployeeWithNullFullName_thenBadRequest() throws Exception {

        employee.setFullName(null);

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test save an employee with empty fullName")
    void whenSaveEmployeeWithEmptyFullName_thenBadRequest() throws Exception {

        employee.setFullName("");

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test save an employee with null email")
    void whenSaveEmployeeWithNullEmail_thenBadRequest() throws Exception {

        employee.setEmail(null);

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test save an employee with empty email")
    void whenSaveEmployeeWithEmptyEmail_thenBadRequest() throws Exception {

        employee.setEmail("");

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test save an employee with null phoneNumber")
    void whenSaveEmployeeWithNullPhoneNumber_thenBadRequest() throws Exception {

        employee.setPhoneNumber(null);

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test save an employee with empty phoneNumber")
    void whenSaveEmployeeWithEmptyPhoneNumber_thenBadRequest() throws Exception {

        employee.setPhoneNumber("");

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test save an employee with existing email")
    void whenSaveEmployeeWithExistingEmail_thenBadRequest() throws Exception {

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test save an employee with existing phoneNumber")
    void whenSaveEmployeeWithExistingPhoneNumber_thenBadRequest() throws Exception {

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test save an employee with email not containing '@'")
    void whenSaveEmployeeWithEmailNotContainingAt_thenBadRequest() throws Exception {

        employee.setEmail("johndoegmail.com");

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test save an employee with email not containing '.'")
    void whenSaveEmployeeWithEmailNotContainingDot_thenBadRequest() throws Exception {

        employee.setEmail("johndoe@gmailcom");

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test save an employee with email containing '@' and '.', but not in the right order")
    void whenSaveEmployeeWithEmailNotInRightOrder_thenBadRequest() throws Exception {

        employee.setEmail("johndoe.gmail@com");

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test save an employee with email containing more then one '@'")
    void whenSaveEmployeeWithEmailContainingMoreThanOneAt_thenBadRequest() throws Exception {

        employee.setEmail("johndoe@@gmail.com");

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test save an employee with email containing only '@.'")
    void whenSaveEmployeeWithEmailContainingOnlyAtDot_thenBadRequest() throws Exception {

        employee.setEmail("@.");

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test save an employee with a phoneNumber not containing only numbers")
    void whenSaveEmployeeWithPhoneNumberNotOnlyNumbers_thenBadRequest() throws Exception {

        employee.setPhoneNumber("123456789a");

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());
    }



}
