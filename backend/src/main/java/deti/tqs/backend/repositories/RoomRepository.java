package deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import deti.tqs.backend.models.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
  
}
