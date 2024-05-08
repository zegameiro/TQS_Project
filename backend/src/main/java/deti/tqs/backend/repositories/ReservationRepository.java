package deti.tqs.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import deti.tqs.backend.models.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
  
  List<Reservation> findByCustomerId(Long userId);

}
