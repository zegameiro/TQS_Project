package deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.backend.models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  
  Employee findByEmail(String email);

  Employee findByFullName(String fullName);

}
