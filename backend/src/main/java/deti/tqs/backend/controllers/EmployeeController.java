package deti.tqs.backend.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.status.Status;
import deti.tqs.backend.dtos.EmployeeSchema;
import deti.tqs.backend.models.Employee;
import deti.tqs.backend.services.EmployeeService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/admin/add")
    public ResponseEntity<Employee> createEmployee(@RequestBody(required = true) EmployeeSchema employeeSchema) {

        

        logger.info("Creating employee");

        Employee employee;
        HttpStatus status = HttpStatus.BAD_REQUEST;

        try {
            employee = new Employee();
            employee.setAdmin(employeeSchema.isAdmin());
            employee.setFullName(employeeSchema.fullName());
            employee.setEmail(employeeSchema.email());
            employee.setPhoneNumber(employeeSchema.phoneNumber());
            employee.setSpecialitiesID(employeeSchema.specialitiesID());


            employee = employeeService.save(employee);
            
            status = HttpStatus.CREATED;
            return new ResponseEntity<Employee>(employee, status);
        } catch (EntityExistsException e) {
            status = HttpStatus.CONFLICT;
        } catch (IllegalArgumentException e) {
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Employee>(status);
    }

}
