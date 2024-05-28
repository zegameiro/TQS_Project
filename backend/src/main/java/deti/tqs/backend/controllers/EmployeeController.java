package deti.tqs.backend.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.backend.dtos.EmployeeSchema;
import deti.tqs.backend.models.Employee;
import deti.tqs.backend.services.EmployeeService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

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
    public ResponseEntity<Employee> createEmployee(@RequestBody(required = true) EmployeeSchema employeeSchema)
            throws Exception {
        // TOTO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
