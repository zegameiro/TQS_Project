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
public class DeleteEmployeeRepositoryTests {

    private EmployeeRepository employeeRepository;

    @Autowired
    DeleteEmployeeRepositoryTests(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private Employee admin;
    private Employee employee;

    /*
     * NECESSARY TESTS
     * 
     * 1. Delete an employee with success
     * 2. Delete an admin employee with success
     */

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();

        admin = new Employee();
        admin.setId(1L);
        admin.setAdmin(true);
        admin.setFullName("John Doe");
        admin.setEmail("johndoe@gmail.com");
        admin.setPhoneNumber("123456789");
        admin.setSpecialitiesID(null);

        employee = new Employee();
        employee.setId(2L);
        employee.setAdmin(false);
        employee.setFullName("John Doe");
        employee.setEmail("maryjane@gmail.com");
        employee.setPhoneNumber("987654321");
        employee.setSpecialitiesID(null);

        employeeRepository.save(admin);
        employeeRepository.save(employee);

    }
    

    @Test
    @DisplayName("Test delete an employee with success")
    void testDeleteEmployeeWithSuccess() {

        employeeRepository.delete(employee);

        Employee foundEmployee = employeeRepository.findById(employee.getId());

        assertThat(foundEmployee).isNull();
    }

    @Test
    @DisplayName("Test delete an admin employee with success")
    void testDeleteAdminEmployeeWithSuccess() {

        employeeRepository.delete(admin);

        Employee foundEmployee = employeeRepository.findById(admin.getId());

        assertThat(foundEmployee).isNull();
    }


}
