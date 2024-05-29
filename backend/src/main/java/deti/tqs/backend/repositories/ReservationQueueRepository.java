package deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.backend.models.ReservationQueue;

@Repository
public interface ReservationQueueRepository extends JpaRepository<ReservationQueue, Long>{
  
}
