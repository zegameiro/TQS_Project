package deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import deti.tqs.backend.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Employee findById(long id);

  Employee findByEmail(String email);

  Employee findByFullName(String fullName);

  Employee findByPhoneNumber(String phoneNumber);

}
