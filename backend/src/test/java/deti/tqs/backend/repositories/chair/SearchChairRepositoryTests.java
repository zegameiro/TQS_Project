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
class SearchChairRepositoryTests {
  
  private ChairRepository chairRepository;
  private RoomRepository roomRepository;

  @Autowired
  SearchChairRepositoryTests(ChairRepository chairRepository, RoomRepository roomRepository) {
    this.chairRepository = chairRepository;
    this.roomRepository = roomRepository;
  }

  private Room room = new Room();
  private Chair chair1 = new Chair();
  private Chair chair2 = new Chair();
  private Chair chair3 = new Chair();
  private Chair chair4 = new Chair();

  /*
   * NECESSARY TESTS
   * 
   *  1. Find a chair by its ID
   *  2. Find a chair by its an inexistent ID
   *  3. Find a chair by its name
   *  4. Find a chair by its name and room ID
   *  5. Find all chairs in a room
   * 
  */

  @BeforeEach
  void setUp() {

    room.setName("Good Room");
    room.setMaxChairsCapacity(10);

    roomRepository.save(room);

    chair1.setName("Chair 1");
    chair1.setRoom(room);

    chair2.setName("Chair 2");
    chair2.setRoom(room);

    chair3.setName("Chair 3");
    chair3.setRoom(room);

    chair4.setName("Chair 4");
    chair4.setRoom(room);

    chairRepository.saveAllAndFlush(List.of(chair1, chair2, chair3, chair4));

  }

  @Test
  @DisplayName("Test find a chair by its ID")
  void testFindChairById() {

    Chair foundChair = chairRepository.findById(chair1.getId());
    assertThat(foundChair).isEqualTo(chair1);

  }

  @Test
  @DisplayName("Test find a chair by an inexistent ID")
  void testFindChairByInexistentId() {

    Chair foundChair = chairRepository.findById(129384);
    assertThat(foundChair).isNull();

  }

  @Test
  @DisplayName("Test find a chair by its name")
  void testFindChairByName() {

    Chair foundChair = chairRepository.findByName("Chair 2");
    assertThat(foundChair).isEqualTo(chair2);

  }

  @Test
  @DisplayName("Test find a chair by its name and room ID")
  void testFindChairByNameAndRoomId() {

    Chair foundChair = chairRepository.findByNameAndRoomId("Chair 3", room.getId());
    assertThat(foundChair).isEqualTo(chair3);

  }

  @Test
  @DisplayName("Test find all chairs in a room")
  void testFindAllChairsInRoom() {

    List<Chair> foundChairs = chairRepository.findByRoomId(room.getId());
    assertThat(foundChairs).hasSize(4).contains(chair1, chair2, chair3, chair4);

  }

}
