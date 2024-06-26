package deti.tqs.backend.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.backend.models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Employee findById(long id);

  Employee findByEmail(String email);

  Employee findByFullName(String fullName);

  Employee findByPhoneNumber(String phoneNumber);

  List<Employee> findByFacility_Id(long facilityId);

}
