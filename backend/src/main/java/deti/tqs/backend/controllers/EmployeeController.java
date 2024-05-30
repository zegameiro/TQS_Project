package deti.tqs.backend.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.backend.dtos.EmployeeSchema;
import deti.tqs.backend.models.Employee;
import deti.tqs.backend.services.EmployeeService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;


import org.slf4j.Logger;

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
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody(required = true) EmployeeSchema employeeSchema) {

        

        logger.info("Creating employee");

        Employee employee;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            employee = new Employee();
            employee.setId(1);
            employee.setFullName(employeeSchema.fullName());
            employee.setEmail(employeeSchema.email());
            employee.setPhoneNumber(employeeSchema.phoneNumber());
            employee.setSpecialitiesID(employeeSchema.specialitiesID());

            employee = employeeService.save(employee);
            
            status = HttpStatus.CREATED;

            return new ResponseEntity<Employee>(employee, status);
        } catch (EntityExistsException e) {
            status = HttpStatus.CONFLICT;
        } catch (NoSuchFieldException e) {
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Employee>(status);
    }

    @PostMapping("/admin/delete")
    public ResponseEntity<Employee> deleteEmployee(@RequestBody(required = true) Long employeeId) {
        try {
            employeeService.remove(employeeId);
            return new ResponseEntity<Employee>(HttpStatus.OK);
        } catch (EntityExistsException e) {
            return new ResponseEntity<Employee>(HttpStatus.CONFLICT);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<Employee>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<Employee>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
