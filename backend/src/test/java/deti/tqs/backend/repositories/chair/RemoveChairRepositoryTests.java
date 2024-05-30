package deti.tqs.backend.repositories.chair;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.ChairRepository;
import deti.tqs.backend.repositories.RoomRepository;

@DataJpaTest
class RemoveChairRepositoryTests {

  private ChairRepository chairRepository;
  private RoomRepository roomRepository;

  @Autowired
  RemoveChairRepositoryTests(ChairRepository chairRepository, RoomRepository roomRepository) {
    this.chairRepository = chairRepository;
    this.roomRepository = roomRepository;
  }

  private Room room = new Room();
  private Chair chair1 = new Chair();
  private Chair chair2 = new Chair();
  private Chair chair3 = new Chair();

  /*
   * NECESSARY TESTS
   * 
   *  1. Remove a chair by its ID and check if it was removed
   *  2. Remove a chair by its entity and check if it was removed
  */

  @BeforeEach
  void setUp() {

    room.setName("Good Room");
    room.setMaxChairsCapacity(10);

    roomRepository.saveAndFlush(room);

    chair1.setName("Chair 1");
    chair1.setRoom(room);

    chair2.setName("Chair 2");
    chair2.setRoom(room);

    chair3.setName("Chair 3");
    chair3.setRoom(room);


    chairRepository.saveAllAndFlush(List.of(chair1, chair2, chair3));

  }

  @Test
  @DisplayName("Remove a chair by its ID and check if it was removed")
  void removeChairById() {

    chairRepository.deleteById(chair1.getId());

    List<Chair> chairs = chairRepository.findAll();

    assertThat(chairs).hasSize(2).doesNotContain(chair1);

  }

  @Test
  @DisplayName("Remove a chair by its entity and check if it was removed")
  void removeChairByEntity() {

    chairRepository.delete(chair3);

    List<Chair> chairs = chairRepository.findAll();

    assertThat(chairs).hasSize(2).doesNotContain(chair3);

  }

}
