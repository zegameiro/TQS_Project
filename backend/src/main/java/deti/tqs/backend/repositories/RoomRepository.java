package deti.tqs.backend.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import deti.tqs.backend.models.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

  Room findById(long id);

  Room findByName(String name);

  List<Room> findByFacilityId(long id);

  List<Room> findByFacilityName(String name);
  
  Room findByNameAndFacilityId(String name, long facilityId);
}
