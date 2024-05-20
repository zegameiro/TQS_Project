package deti.tqs.backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import deti.tqs.backend.models.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

  Room findById(long id);

  Room findByName(String name);

  Room findByFacilityId(long id);
  
  Room findByNameAndFacilityId(String name, long facilityId);
}
