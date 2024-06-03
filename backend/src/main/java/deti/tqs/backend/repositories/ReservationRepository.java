package deti.tqs.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.backend.models.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
  
  Reservation findById(long id);

  List<Reservation> findByEmployeeId(Long employeeId);

  Reservation findBySecretCode(String secretCode);

  List<Reservation> findByCustomerEmail(String email);

}
