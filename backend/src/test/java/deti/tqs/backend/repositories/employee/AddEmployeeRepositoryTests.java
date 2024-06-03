package deti.tqs.backend.repositories.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.backend.models.Employee;
import deti.tqs.backend.repositories.EmployeeRepository;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class AddEmployeeRepositoryTests {

    private EmployeeRepository employeeRepository;

    @Autowired
    AddEmployeeRepositoryTests(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private Employee employee;

    /*
     * NECESSARY TESTS
     * 
     * 1. Save an employee with success and find it by id
     * 2. Fail to save an employee with null fullName
     * 3. Fail to save an employee with null email
     * 4. Fail to save an employee with null phoneNumber
     * 5. Fail to save an employee with existing email
     * 6. Fail to save an employee with existing phoneNumber
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
    @DisplayName("Test save an employee with success and find it by id")
    void testSaveEmployeeWithSuccess() {

        employeeRepository.save(employee);

        Employee foundEmployee = employeeRepository.findById(employee.getId());

        assertAll(
                () -> assertThat(foundEmployee).isNotNull(),
                () -> assertThat(foundEmployee.getFullName()).isEqualTo(employee.getFullName()),
                () -> assertThat(foundEmployee.getEmail()).isEqualTo(employee.getEmail()),
                () -> assertThat(foundEmployee.getPhoneNumber()).isEqualTo(employee.getPhoneNumber()),
                () -> assertThat(foundEmployee.getSpecialitiesID()).isEqualTo(employee.getSpecialitiesID()));
    }

    @Test
    @DisplayName("Test fail to save an employee with null fullName")
    void testFailToSaveEmployeeWithNullFullName() {

        employee.setFullName(null);

        assertThatThrownBy(
                () -> employeeRepository.save(employee))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("Test fail to save an employee with null email")
    void testFailToSaveEmployeeWithNullEmail() {

        employee.setEmail(null);

        assertThatThrownBy(
                () -> employeeRepository.save(employee))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("Test fail to save an employee with null phoneNumber")
    void testFailToSaveEmployeeWithNullPhoneNumber() {

        employee.setPhoneNumber(null);

        assertThatThrownBy(
                () -> employeeRepository.save(employee))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("Test fail to save an employee with existing email")
    void testFailToSaveEmployeeWithExistingEmail() {

        employeeRepository.save(employee);

        Employee employee2 = new Employee();
        employee2.setFullName("John Doe Jr");
        employee2.setEmail("johndoe@gmail.com");
        employee2.setPhoneNumber("987654321");
        employee2.setSpecialitiesID(null);

        assertThatThrownBy(
                () -> employeeRepository.save(employee2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Test fail to save an employee with existing phoneNumber")
    void testFailToSaveEmployeeWithExistingPhoneNumber() {

        employeeRepository.save(employee);

        Employee employee2 = new Employee();
        employee2.setFullName("John Doe Jr");
        employee2.setEmail("johndoejr@gmail.com");
        employee2.setPhoneNumber("123456789");
        employee2.setSpecialitiesID(null);

        assertThatThrownBy(
                () -> employeeRepository.save(employee2))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

}
