package deti.tqs.backend.controllers.employee;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.backend.JsonUtils;
import deti.tqs.backend.models.Employee;
import deti.tqs.backend.services.EmployeeService;

@WebMvcTest(EmplyeeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CreateEmplyeeControllerTests {
    
    private MockMvc mvc;

    @Autowired
    CreateEmplyeeControllerTests(MockMvc mvc) {
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
        employeeRepository.deleteAll();

        employee = new Employee();
        employee.setId(1L);
        employee.setFullName("Employee 1");
        employee.setEmail("johndoe@gmail.com");
        employee.setPhoneNumber("123456789");
        employee.setSpecialities(null);

    }

    @Test
    @DisplayName("Test save an employee with success")
    void whenSaveEmployee_thenCreateEmployee() {

        when(employeeService.save(any())).thenReturn(employee);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is((int) employee.getId())))
        .andExpect(jsonPath("$.fullName", is(employee.getFullName())))
        .andExpect(jsonPath("$.email", is(employee.getEmail())))
        .andExpect(jsonPath("$.phoneNumber", is(employee.getPhoneNumber())));

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with null fullName")
    void whenSaveEmployeeWithNullFullName_thenBadRequest() {

        employee.setFullName(null);

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with empty fullName")
    void whenSaveEmployeeWithEmptyFullName_thenBadRequest() {

        employee.setFullName("");

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with null email")
    void whenSaveEmployeeWithNullEmail_thenBadRequest() {

        employee.setEmail(null);

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with empty email")
    void whenSaveEmployeeWithEmptyEmail_thenBadRequest() {

        employee.setEmail("");

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with null phoneNumber")
    void whenSaveEmployeeWithNullPhoneNumber_thenBadRequest() {

        employee.setPhoneNumber(null);

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with empty phoneNumber")
    void whenSaveEmployeeWithEmptyPhoneNumber_thenBadRequest() {

        employee.setPhoneNumber("");

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with existing email")
    void whenSaveEmployeeWithExistingEmail_thenBadRequest() {

        when(employeeService.save(any())).thenThrow(IllegalArgumentException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with existing phoneNumber")
    void whenSaveEmployeeWithExistingPhoneNumber_thenBadRequest() {

        when(employeeService.save(any())).thenThrow(IllegalArgumentException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with email not containing '@'")
    void whenSaveEmployeeWithEmailNotContainingAt_thenBadRequest() {

        employee.setEmail("johndoegmail.com");

        when(employeeService.save(any())).thenThrow(IllegalArgumentException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with email not containing '.'")
    void whenSaveEmployeeWithEmailNotContainingDot_thenBadRequest() {

        employee.setEmail("johndoe@gmailcom");

        when(employeeService.save(any())).thenThrow(IllegalArgumentException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with email containing '@' and '.', but not in the right order")
    void whenSaveEmployeeWithEmailNotInRightOrder_thenBadRequest() {

        employee.setEmail("johndoe.gmail@com");

        when(employeeService.save(any())).thenThrow(IllegalArgumentException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with email containing more then one '@'")
    void whenSaveEmployeeWithEmailContainingMoreThanOneAt_thenBadRequest() {

        employee.setEmail("johndoe@@gmail.com");

        when(employeeService.save(any())).thenThrow(IllegalArgumentException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with email containing only '@.'")
    void whenSaveEmployeeWithEmailContainingOnlyAtDot_thenBadRequest() {

        employee.setEmail("@.");

        when(employeeService.save(any())).thenThrow(IllegalArgumentException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test save an employee with a phoneNumber not containing only numbers")
    void whenSaveEmployeeWithPhoneNumberNotOnlyNumbers_thenBadRequest() {

        employee.setPhoneNumber("123456789a");

        when(employeeService.save(any())).thenThrow(IllegalArgumentException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).save(any());
    }

    

}
