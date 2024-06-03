package deti.tqs.backend.services.employee;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import jakarta.persistence.EntityExistsException;

import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.backend.models.Employee;
import deti.tqs.backend.repositories.EmployeeRepository;
import deti.tqs.backend.services.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class DeleteEmployeeServiceTests {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setAdmin(false);
        employee.setFullName("John Doe");
        employee.setEmail("johndoe@gmail.com");
        employee.setPhoneNumber("123456789");
        employee.setSpecialitiesID(null);
    }

    @Test
    @DisplayName("Test delete an admin with success")
    void whenDeleteAdmin_thenRemoveAdmin() throws Exception {
        employee.setAdmin(true);
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));
        when(employeeRepository.count()).thenReturn(2L);

        Boolean isDeleted = employeeService.remove(1L);

        assertThat(isDeleted).isTrue();
        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    @DisplayName("Test fail to delete an employee with null id")
    void whenDeleteEmployeeWithNullId_thenThrowException() {
        assertThatThrownBy(() -> employeeService.remove(null))
            .isInstanceOf(NoSuchFieldException.class)
            .hasMessage("Employee with this ID does not exist");

        verify(employeeRepository, times(0)).delete(employee);
    }

    @Test
    @DisplayName("Test fail to delete an admin with null id")
    void whenDeleteAdminWithNullId_thenThrowException() {
        employee.setAdmin(true);

        assertThatThrownBy(() -> employeeService.remove(null))
            .isInstanceOf(NoSuchFieldException.class)
            .hasMessage("Employee with this ID does not exist");

        verify(employeeRepository, times(0)).delete(employee);
    }

    @Test
    @DisplayName("Test fail to delete the only admin")
    void whenDeleteTheOnlyAdmin_thenThrowException() {
        employee.setAdmin(true);
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));
        when(employeeRepository.count()).thenReturn(1L);

        assertThatThrownBy(() -> employeeService.remove(1L))
            .isInstanceOf(EntityExistsException.class)
            .hasMessage("Cannot delete last admin");

        verify(employeeRepository, times(0)).delete(employee);
    }
}
