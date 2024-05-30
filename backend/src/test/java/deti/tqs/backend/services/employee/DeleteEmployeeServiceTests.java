package deti.tqs.backend.services.employee;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityExistsException;

import org.hibernate.mapping.Any;
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

    /*
     * NECESSARY TESTS
     * 
     * 1. Save an employee with success
     * 2. Fail to save an employee with null fullName
     * 3. Fail to save an employee with empty fullName
     * 4. Fail to save an employee with null email
     * 5. Fail to save an employee with empty email
     * 6. Fail to save an employee with null phoneNumber
     * 7. Fail to save an employee with empty phoneNumber
     * 8. Fail to save an employee with existing email
     * 9. Fail to save an employee with existing phoneNumber
     * 10. Fail to save an employee with email not containing '@'
     * 11. Fail to save an employee with email not containing '.'
     * 12. Fail to save an employee with email containing '@' and '.', but not in the right order
     * 13. Fail to save an employee with email containing more then one '@'
     * 14. Fail to save an employee with email containing only '@.'
     * 13. Fail to save an employee with a phoneNumber not containing only numbers
     */

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();

        employee = new Employee();
        employee.setId(1L);
        employee.setAdmin(false);
        employee.setFullName("John Doe");
        employee.setEmail("johndoe@gmail.com");
        employee.setPhoneNumber("123456789");
        employee.setSpecialitiesID(null);

    }

    @Test
    @DisplayName("Test save an employee with success")
    void whenSaveEmployee_thenCreateEmployee() {

        employee.setId(1L);

        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee savedEmployee = employeeRepository.save(employee);

        assertAll(
            () -> assertThat(savedEmployee).isNotNull(),
            () -> assertThat(savedEmployee.getFullName()).isEqualTo(employee.getFullName()),
            () -> assertThat(savedEmployee.getEmail()).isEqualTo(employee.getEmail()),
            () -> assertThat(savedEmployee.getPhoneNumber()).isEqualTo(employee.getPhoneNumber())
        );

        verify(employeeRepository, times(1)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with null fullName")
    void whenSaveEmployeeWithNullFullName_thenThrowException() {

        employee.setFullName(null);

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have a full name");

        verify(employeeRepository, times(0)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with an empty fullName")
    void whenSaveEmployeeWithEmptyFullName_thenThrowException() {

        employee.setFullName("");

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have a full name");

        verify(employeeRepository, times(0)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with null email")
    void whenSaveEmployeeWithNullEmail_thenThrowException() {

        employee.setEmail(null);

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have an email");

        verify(employeeRepository, times(0)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with empty email")
    void whenSaveEmployeeWithEmptyEmail_thenThrowException() {

        employee.setEmail("");

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have an email");

        verify(employeeRepository, times(0)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with null phoneNumber")
    void whenSaveEmployeeWithNullPhoneNumber_thenThrowException() {

        employee.setPhoneNumber(null);

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have a phone number");

        verify(employeeRepository, times(0)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with empty phoneNumber")
    void whenSaveEmployeeWithEmptyPhoneNumber_thenThrowException() {

        employee.setPhoneNumber("");

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have a phone number");

        verify(employeeRepository, times(0)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with existing email")
    void whenSaveEmployeeWithExistingEmail_thenThrowException() {

        when(employeeRepository.findByEmail("johndoe@gmail.com")).thenReturn(employee);

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Employee with this email already exists");

        verify(employeeRepository, times(0)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with existing phoneNumber")
    void whenSaveEmployeeWithExistingPhoneNumber_thenThrowException() {

        when(employeeRepository.findByPhoneNumber("123456789")).thenReturn(employee);

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Employee with this phone number already exists");

        verify(employeeRepository, times(0)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with email not containing '@'")
    void whenSaveEmployeeWithEmailNotContainingAt_thenThrowException() {

        employee.setEmail("johndoegmail.com");

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have a valid email");

        verify(employeeRepository, times(0)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with email not containing '.'")
    void whenSaveEmployeeWithEmailNotContainingDot_thenThrowException() {

        employee.setEmail("johndoe@gmailcom");

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have a valid email");

        verify(employeeRepository, times(0)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with email containing '@' and '.', but not in the right order")
    void whenSaveEmployeeWithEmailNotInRightOrder_thenThrowException() {

        employee.setEmail("johndoe.gmail@com");

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have a valid email");

        verify(employeeRepository, times(0)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with email containing more then one '@'")
    void whenSaveEmployeeWithEmailContainingMoreThanOneAt_thenThrowException() {

        employee.setEmail("johndoe@@gmail.com");

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have a valid email");

        verify(employeeRepository, times(0)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with email containing only '@.'")
    void whenSaveEmployeeWithEmailContainingOnlyAtDot_thenThrowException() {

        employee.setEmail("@.");

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have a valid email");

        verify(employeeRepository, times(0)).save(employee);

    }

    @Test
    @DisplayName("Test fail to save an employee with a phoneNumber not containing only numbers")
    void whenSaveEmployeeWithPhoneNumberNotContainingOnlyNumbers_thenThrowException() {

        employee.setPhoneNumber("123456789a");

        assertThatThrownBy(
                () -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have a valid phone number");

        verify(employeeRepository, times(0)).save(employee);

    }


}
