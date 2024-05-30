package deti.tqs.backend.controllers.employee;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.backend.controllers.EmployeeController;
import deti.tqs.backend.models.Employee;
import deti.tqs.backend.services.EmployeeService;
import jakarta.persistence.EntityExistsException;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DeleteEmployeeControllerTests {

    private MockMvc mvc;

    @Autowired
    public DeleteEmployeeControllerTests(MockMvc mvc) {
        this.mvc = mvc;
    }

    @MockBean
    private EmployeeService employeeService;

    private Employee employee = new Employee();

    private NoSuchFieldException employeeDoesntExist = new NoSuchFieldException("Employee with this ID does not exist");
    private String nullID = "{\"employeeId\": null}";
    private String validID = "{\"employeeId\": 1}";
    private String invalidID = "{\"employeeId\": 3}";

    /*
     * NECESSARY TESTS
     * 
     * 1. Delete an employee with success
     * 2. Delete an admin with success
     * 2. Fail to delete an employee with 
     *          null id
     *          invalid id
     * 5. Fail to delete an admin with 
     *          null id
     *          invalid id
     * 6. Fail to delete the only admin
     */

    @Test
    @DisplayName("Test receive status code isOk when deleting an employee with success")
    void testDeleteEmployeeWithSuccess() throws Exception {
        when(employeeService.remove(any())).thenReturn(true);

        mvc.perform(post("/api/employee/admin/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .content(validID))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test receive status code badRequest when deleting an employee with null id")
    void testDeleteEmployeeWithNullId() throws Exception {

        when(employeeService.remove(any())).thenThrow(employeeDoesntExist);

        mvc.perform(post("/api/employee/admin/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .content(nullID))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test receive status code badRequest when deleting an employee with invalid id")
    void testDeleteEmployeeWithInvalidId() throws Exception {
        when(employeeService.remove(any())).thenThrow(employeeDoesntExist);

        mvc.perform(post("/api/employee/admin/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidID))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test receive status code isOk when deleting an admin with success")
    void testDeleteAdminWithSuccess() throws Exception {

        when(employeeService.remove(any())).thenReturn(true);

        mvc.perform(post("/api/employee/admin/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .content(validID))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test receive status code badRequest when deleting an admin with null id")
    void testDeleteAdminWithNullId() throws Exception {

        when(employeeService.remove(any())).thenThrow(employeeDoesntExist);

        mvc.perform(post("/api/employee/admin/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .content(nullID))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test receive status code badRequest when deleting the only admin")
    void testDeleteOnlyAdmin() throws Exception {

        when(employeeService.remove(any())).thenThrow(new EntityExistsException("Cannot delete last admin"));

        mvc.perform(post("/api/employee/admin/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .content(validID))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Test receive status code badRequest when deleting an admin with invalid id")
    void testDeleteAdminWithInvalidId() throws Exception {

        when(employeeService.remove(any())).thenThrow(employeeDoesntExist);

        mvc.perform(post("/api/employee/admin/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidID))
            .andExpect(status().isBadRequest());
    }
}
