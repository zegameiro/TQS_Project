package deti.tqs.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import deti.tqs.backend.models.Employee;
import deti.tqs.backend.repositories.EmployeeRepository;
import jakarta.persistence.EntityExistsException;
import java.lang.NoSuchFieldException;

import javax.xml.stream.FactoryConfigurationError;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee save(Employee employee) throws NoSuchFieldException, EntityExistsException {

        checkIfEntityExists(employee);
        checkIfEntityIsValid(employee);
        checkIfEmailIsValid(employee);
        checkIfPhoneNumberIsValid(employee);

        return employeeRepository.save(employee);
    }

    public Boolean remove(Long employeeID) throws NoSuchFieldException, EntityExistsException {
        Employee employee = employeeRepository.findById(employeeID).orElse(null);
        if (employee == null)
            throw new NoSuchFieldException("Employee with this ID does not exist");
        if (employee.isAdmin())
            if (employeeRepository.count() == 1)
                throw new EntityExistsException("Cannot delete last admin");
        employeeRepository.delete(employee);
        return true;
    }

    private void checkIfEntityExists(Employee employee) throws EntityExistsException {
        if (employeeRepository.findByEmail(employee.getEmail()) != null)
            throw new EntityExistsException("Employee with this email already exists"); 
        if (employeeRepository.findByPhoneNumber(employee.getPhoneNumber()) != null)
            throw new EntityExistsException("Employee with this phone number already exists");
    }

    private void checkIfEntityIsValid(Employee employee) throws NoSuchFieldException {
        if (isNullOrEmpty(employee.getFullName()))
            throw new NoSuchFieldException("Employee must have a full name");

        if (isNullOrEmpty(employee.getEmail()))
            throw new NoSuchFieldException("Employee must have an email");

        if (isNullOrEmpty(employee.getPhoneNumber()))
            throw new NoSuchFieldException("Employee must have a phone number");
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private void checkIfEmailIsValid(Employee employee) throws NoSuchFieldException {
        if (!employee.getEmail().matches("^(.+)@(.+)\\.(.+)$"))
            throw new NoSuchFieldException("Employee must have a valid email");
        if (employee.getEmail().chars().filter(ch -> ch == '@').count() != 1)
            throw new NoSuchFieldException("Employee must have a valid email");
    }

    private void checkIfPhoneNumberIsValid(Employee employee) throws NoSuchFieldException {
        if (!employee.getPhoneNumber().matches("^[0-9]+$"))
            throw new NoSuchFieldException("Employee must have a valid phone number");
    }
}

