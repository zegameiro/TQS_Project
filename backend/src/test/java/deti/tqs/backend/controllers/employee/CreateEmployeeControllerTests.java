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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
import jakarta.persistence.EntityExistsException;

import java.lang.NoSuchFieldException;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class CreateEmployeeControllerTests {
    
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
     * 2. Receive status code badRequest when saving an employee with 
     *          null fullName
     *          empty fullName
     * 4. Receive status code badRequest when saving an employee with 
     *          null email
     *          empty email
     *          not containing '@'
     *          not containing '.'
     *          containing '@' and '.', but in the wrong order
     *          containing more then one '@'
     *          containing only '@.'
     * 11. Receive status code badRequest when saving an employee with 
     *          null phoneNumber
     *          empty phoneNumber
     *          not containing only numbers
     *          with less then 9 digits
     *          with more then 9 digits
     * 16. Receive status code badRequest when saving an employee with existing email
     * 17. Receive status code badRequest when saving an employee with existing phoneNumber
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
    @DisplayName("Test save an employee with existing email")
    void whenSaveEmployeeWithExistingEmail_thenBadRequest() throws Exception {

        when(employeeService.save(any())).thenThrow(EntityExistsException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Test save an employee with existing phoneNumber")
    void whenSaveEmployeeWithExistingPhoneNumber_thenBadRequest() throws Exception {

        when(employeeService.save(any())).thenThrow(EntityExistsException.class);

        mvc.perform(
            post("/api/employee/admin/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(employee)
        ))
        .andExpect(status().isConflict());
    }

    private static Stream<String> invalidNames() {
        return Stream.of(
                null, // null full name
                "" // empty full name
        );
    }

    @ParameterizedTest
    @MethodSource("invalidNames")
    @DisplayName("Test save an employee with invalid emails")
    void whenSaveEmployeeWithInvalidName_thenBadRequest(String invalidName) throws Exception {
        Employee employee = new Employee();
        employee.setFullName(invalidName);

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(employee))
        )
        .andExpect(status().isBadRequest());
    }

    private static Stream<String> invalidEmails() {
        return Stream.of(
                null, // null email
                "", // empty email
                "johndoegmail.com", // email not containing '@'
                "johndoe@gmailcom", // email not containing '.'
                "johndoe.gmail@com", // email with '@' and '.', but in the wrong order
                "johndoe@@gmail.com", // email containing more than one '@'
                "@." // email containing only '@.'
        );
    }

    @ParameterizedTest
    @MethodSource("invalidEmails")
    @DisplayName("Test save an employee with invalid emails")
    void whenSaveEmployeeWithInvalidEmail_thenBadRequest(String invalidEmail) throws Exception {
        Employee employee = new Employee();
        employee.setEmail(invalidEmail);

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(employee))
        )
        .andExpect(status().isBadRequest());
    }

    private static Stream<String> invalidNumbers() {
        return Stream.of(
                null, // null phone number
                "", // empty phone number
                "123456789a", // phone number containing letters
                "1230", // phone number with less then 9 digits
                "1234567890" // phone number with more then 9 digits
        );
    }

    @ParameterizedTest
    @MethodSource("invalidNumbers")
    @DisplayName("Test save an employee with invalid emails")
    void whenSaveEmployeeWithInvalidPhoneNumber_thenBadRequest(String invalidNumber) throws Exception {
        Employee employee = new Employee();
        employee.setPhoneNumber(invalidNumber);

        when(employeeService.save(any())).thenThrow(NoSuchFieldException.class);

        mvc.perform(
            post("/api/employee/admin/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(employee))
        )
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
