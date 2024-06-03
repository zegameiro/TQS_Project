package deti.tqs.backend.repositories.room;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.RoomRepository;

@DataJpaTest
class DeleteRoomRepositoryTests {
  
  private RoomRepository roomRepository;

  @Autowired
  DeleteRoomRepositoryTests(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  /*
   * NECESSARY TESTS
   * 
   *  1. Save a room with success and delete it
   * 
  */

  @Test
  @DisplayName("Test save a room with success and delete it")
  void testSaveRoomAndDeleteIt() {

    Room r = new Room();
    r.setName("Room 1");
    r.setMaxChairsCapacity(10);
    r.setBeautyServiceId(2);

    roomRepository.save(r);

    Room foundRoom = roomRepository.findById(r.getId());

    assertAll(
      () -> assertThat(foundRoom).isNotNull(),
      () -> assertThat(foundRoom.getName()).isEqualTo(r.getName())
    );

    roomRepository.delete(r);

    Room deletedRoom = roomRepository.findById(r.getId());

    assertThat(deletedRoom).isNull();

  }

}
