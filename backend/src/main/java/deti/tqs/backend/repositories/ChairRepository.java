package deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import deti.tqs.backend.models.Chair;

public interface ChairRepository extends JpaRepository<Chair, Long> {
  
  Chair findById(long id);

  Chair findByName(String name);

  Chair findByNameAndRoomId(String name, long roomId);

}
