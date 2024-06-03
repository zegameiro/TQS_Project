package deti.tqs.backend.services.employee;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import jakarta.persistence.EntityExistsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.backend.models.Employee;
import deti.tqs.backend.repositories.EmployeeRepository;
import deti.tqs.backend.services.EmployeeService;

import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class CreateEmployeeServiceTests {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    private Employee employee;

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
        employee.setFullName("John Doe");
        employee.setEmail("johndoe@gmail.com");
        employee.setPhoneNumber("123456789");
        employee.setSpecialitiesID(null);
    }
    
    @Test
    @DisplayName("Test save an employee with success")
    void whenSaveEmployee_thenCreateEmployee() throws Exception {
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee savedEmployee = employeeService.save(employee);

        assertAll(
            () -> assertThat(savedEmployee).isNotNull(),
            () -> assertThat(savedEmployee.getFullName()).isEqualTo(employee.getFullName()),
            () -> assertThat(savedEmployee.getEmail()).isEqualTo(employee.getEmail()),
            () -> assertThat(savedEmployee.getPhoneNumber()).isEqualTo(employee.getPhoneNumber())
        );

        verify(employeeRepository, times(1)).save(employee);
    }

    private static Stream<String> invalidNames() {
        return Stream.of(
                null, // null full name
                "" // empty full name
        );
    }

    @ParameterizedTest
    @MethodSource("invalidNames")
    @DisplayName("Test fail to save an employee with invalid full names")
    void whenSaveEmployeeWithInvalidName_thenThrowException(String invalidName) {
        employee.setFullName(invalidName);

        assertThatThrownBy(() -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have a full name");

        verify(employeeRepository, times(0)).save(employee);
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
    @DisplayName("Test fail to save an employee with invalid emails")
    void whenSaveEmployeeWithInvalidEmail_thenThrowException(String invalidEmail) {
        employee.setEmail(invalidEmail);

        assertThatThrownBy(() -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have a valid email");

        verify(employeeRepository, times(0)).save(employee);
    }

    private static Stream<String> invalidNumbers() {
        return Stream.of(
                null, // null phone number
                "", // empty phone number
                "123456789a", // phone number containing letters
                "1230", // phone number with less than 9 digits
                "1234567890" // phone number with more than 9 digits
        );
    }

    @ParameterizedTest
    @MethodSource("invalidNumbers")
    @DisplayName("Test fail to save an employee with invalid phone numbers")
    void whenSaveEmployeeWithInvalidPhoneNumber_thenThrowException(String invalidNumber) {
        employee.setPhoneNumber(invalidNumber);

        assertThatThrownBy(() -> employeeService.save(employee))
                .isInstanceOf(NoSuchFieldException.class)
                .hasMessage("Employee must have a valid phone number");

        verify(employeeRepository, times(0)).save(employee);
    }

    @Test
    @DisplayName("Test fail to save an employee with existing email")
    void whenSaveEmployeeWithExistingEmail_thenThrowException() {
        when(employeeRepository.findByEmail("johndoe@gmail.com")).thenReturn(employee);

        assertThatThrownBy(() -> employeeService.save(employee))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Employee with this email already exists");

        verify(employeeRepository, times(0)).save(employee);
    }

    @Test
    @DisplayName("Test fail to save an employee with existing phoneNumber")
    void whenSaveEmployeeWithExistingPhoneNumber_thenThrowException() {
        when(employeeRepository.findByPhoneNumber("123456789")).thenReturn(employee);

        assertThatThrownBy(() -> employeeService.save(employee))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Employee with this phone number already exists");

        verify(employeeRepository, times(0)).save(employee);
    }
}
