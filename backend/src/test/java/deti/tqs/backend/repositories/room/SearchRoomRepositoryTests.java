package deti.tqs.backend.repositories.room;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.RoomRepository;

@DataJpaTest
class SearchRoomRepositoryTests {
  
  private RoomRepository roomRepository;

  private FacilityRepository facilityRepository;

  @Autowired
  SearchRoomRepositoryTests(RoomRepository roomRepository, FacilityRepository facilityRepository) {
    this.roomRepository = roomRepository;
    this.facilityRepository = facilityRepository;
  }

  /*
   * NECESSARY TESTS
   *  
   *  1. Find a room by id
   *  2. Find a room by an id that doesn't exist
   *  3. Find a room by name
   *  4. Find a room by a name that doesn't exist
   *  5. Find a room by facility id
   *  6. Find a room by a facility id that doesn't exist
   *  7. Find a room by facility name
   *  8. Find a room by a facility name that doesn't exist
   *  9. Find a room by name and facility id
   *  10. Find all available rooms
   * 
  */

  @BeforeEach
  void setUp() {

    roomRepository.deleteAll();
    facilityRepository.deleteAll();

  }

  @Test
  @DisplayName("Test find a room by id")
  void testFindRoomById() {

    Facility f = new Facility();
    f.setName("Facility 1");
    f.setCity("Aveiro");
    f.setPhoneNumber("123456789");
    f.setStreetName("Rua do Aveiro");
    f.setPostalCode("1234-567");
    f.setMaxRoomsCapacity(100);

    facilityRepository.saveAndFlush(f);

    Room r = new Room();
    r.setName("Room 1");
    r.setMaxChairsCapacity(10);
    r.setFacility(f);
    r.setBeautyServiceId(0);

    roomRepository.saveAndFlush(r);

    Room foundRoom = roomRepository.findById(r.getId());

    assertAll(
      () -> assertThat(foundRoom).isNotNull(),
      () -> assertThat(foundRoom.getName()).isEqualTo("Room 1"),
      () -> assertThat(foundRoom.getMaxChairsCapacity()).isEqualTo(10),
      () -> assertThat(foundRoom.getFacility()).isEqualTo(f),
      () -> assertThat(foundRoom.getBeautyServiceId()).isEqualTo(0)
    );

  }

  @Test
  @DisplayName("Test find a room by an id that doesn't exist")
  void testFindRoomByNonExistingId() {

    Room foundRoom = roomRepository.findById(16L);

    assertThat(foundRoom).isNull();

  }

  @Test
  @DisplayName("Test find a room by name")
  void testFindRoomByName() {

    Facility f = new Facility();
    f.setName("Facility 2");
    f.setCity("Lisboa");
    f.setPhoneNumber("987654321");
    f.setStreetName("Rua do Lisboa");
    f.setPostalCode("8263-102");
    f.setMaxRoomsCapacity(200);

    facilityRepository.saveAndFlush(f);

    Room r = new Room();
    r.setName("Room 2");
    r.setMaxChairsCapacity(20);
    r.setFacility(f);
    r.setBeautyServiceId(1);

    roomRepository.saveAndFlush(r);

    Room foundRoom = roomRepository.findByName("Room 2");

    assertAll(
      () -> assertThat(foundRoom).isNotNull(),
      () -> assertThat(foundRoom.getName()).isEqualTo("Room 2"),
      () -> assertThat(foundRoom.getMaxChairsCapacity()).isEqualTo(20),
      () -> assertThat(foundRoom.getFacility()).isEqualTo(f),
      () -> assertThat(foundRoom.getBeautyServiceId()).isEqualTo(1)
    );

  }

  @Test
  @DisplayName("Test find a room by a name that doesn't exist")
  void testFindRoomByNonExistingName() {

    Room foundRoom = roomRepository.findByName("Room De Leiria");

    assertThat(foundRoom).isNull();

  }

  @Test
  @DisplayName("Test find a room by facility id")
  void testFindRoomByFacilityId() {

    Facility f = new Facility();
    f.setName("Facility 3");
    f.setCity("Porto");
    f.setPhoneNumber("987654321");
    f.setStreetName("Rua do Porto");
    f.setPostalCode("8263-102");
    f.setMaxRoomsCapacity(200);

    facilityRepository.saveAndFlush(f);

    Room r1 = new Room();
    r1.setName("Room 3");
    r1.setMaxChairsCapacity(30);
    r1.setFacility(f);
    r1.setBeautyServiceId(2);

    Room r2 = new Room();
    r2.setName("Room 4");
    r2.setMaxChairsCapacity(40);
    r2.setFacility(f);
    r2.setBeautyServiceId(4);

    roomRepository.saveAllAndFlush(List.of(r1, r2));

    List<Room> foundRoom = roomRepository.findByFacilityId(f.getId());

    assertAll(
      () -> assertThat(foundRoom).isNotNull(),
      () -> assertThat(foundRoom.size()).isEqualTo(2),
      () -> assertThat(foundRoom.get(0)).isEqualTo(r1),
      () -> assertThat(foundRoom.get(1)).isEqualTo(r2)
    );

  }

  @Test
  @DisplayName("Test find a room by a facility id that doesn't exist")
  void testFindRoomByNonExistingFacilityId() {

    List<Room> foundRoom = roomRepository.findByFacilityId(16L);

    assertThat(foundRoom).isEmpty();

  }

  @Test
  @DisplayName("Test find a room by facility name")
  void testFindRoomByFacilityName() {

    Facility f = new Facility();
    f.setName("Facility 1");
    f.setCity("Aveiro");
    f.setPhoneNumber("123456789");
    f.setStreetName("Rua do Aveiro");
    f.setPostalCode("1234-567");
    f.setMaxRoomsCapacity(100);

    facilityRepository.saveAndFlush(f);

    Room r1 = new Room();
    r1.setName("Room 5");
    r1.setMaxChairsCapacity(50);
    r1.setFacility(f);
    r1.setBeautyServiceId(1);

    Room r2 = new Room();
    r2.setName("Room 6");
    r2.setMaxChairsCapacity(60);
    r2.setFacility(f);
    r2.setBeautyServiceId(2);

    roomRepository.saveAllAndFlush(List.of(r1, r2));

    List<Room> foundRoom = roomRepository.findByFacilityName("Facility 1");

    assertAll(
      () -> assertThat(foundRoom).isNotNull(),
      () -> assertThat(foundRoom.size()).isEqualTo(2),
      () -> assertThat(foundRoom.get(0)).isEqualTo(r1),
      () -> assertThat(foundRoom.get(1)).isEqualTo(r2)
    );

  }

  @Test
  @DisplayName("Test find a room by a facility name that doesn't exist")
  void testFindRoomByNonExistingFacilityName() {

    List<Room> foundRoom = roomRepository.findByFacilityName("Facility De Leiria");

    assertThat(foundRoom).isEmpty();

  }

  @Test
  @DisplayName("Test find a room by name and facility id")
  void testFindRoomByNameAndFacilityId() {

    Facility f = new Facility();
    f.setName("Facility 4");
    f.setCity("Faro");
    f.setPhoneNumber("987654321");
    f.setStreetName("Rua do Faro");
    f.setPostalCode("8263-102");
    f.setMaxRoomsCapacity(200);

    facilityRepository.saveAndFlush(f);

    Room r1 = new Room();
    r1.setName("Room 7");
    r1.setMaxChairsCapacity(70);
    r1.setFacility(f);
    r1.setBeautyServiceId(0);

    roomRepository.saveAndFlush(r1);

    Room foundRoom = roomRepository.findByNameAndFacilityId("Room 7", f.getId());

    assertAll(
      () -> assertThat(foundRoom).isNotNull(),
      () -> assertThat(foundRoom.getName()).isEqualTo("Room 7"),
      () -> assertThat(foundRoom.getMaxChairsCapacity()).isEqualTo(70),
      () -> assertThat(foundRoom.getFacility()).isEqualTo(f)
    );

  }

  @Test
  @DisplayName("Test find all available rooms")
  void testFindAllRooms() {

    Facility f = new Facility();
    f.setName("Facility 5");
    f.setCity("Faro");
    f.setPhoneNumber("987654321");
    f.setStreetName("Rua do Faro");
    f.setPostalCode("8263-102");
    f.setMaxRoomsCapacity(200);

    facilityRepository.saveAndFlush(f);

    Room r1 = new Room();
    r1.setName("Room 8");
    r1.setMaxChairsCapacity(80);
    r1.setFacility(f);
    r1.setBeautyServiceId(1);

    Room r2 = new Room();
    r2.setName("Room 9");
    r2.setMaxChairsCapacity(90);
    r2.setFacility(f);
    r2.setBeautyServiceId(2);

    Room r3 = new Room();
    r3.setName("Room 10");
    r3.setMaxChairsCapacity(100);
    r3.setFacility(f);
    r3.setBeautyServiceId(3);

    Room r4 = new Room();
    r4.setName("Room 11");
    r4.setMaxChairsCapacity(110);
    r4.setFacility(f);
    r4.setBeautyServiceId(4);

    roomRepository.saveAllAndFlush(List.of(r1, r2, r3, r4));

    List<Room> foundRoom = roomRepository.findAll();

    assertAll(
      () -> assertThat(foundRoom).isNotNull(),
      () -> assertThat(foundRoom.size()).isEqualTo(4),
      () -> assertThat(foundRoom.get(0)).isEqualTo(r1),
      () -> assertThat(foundRoom.get(1)).isEqualTo(r2),
      () -> assertThat(foundRoom.get(2)).isEqualTo(r3),
      () -> assertThat(foundRoom.get(3)).isEqualTo(r4)
    );

  }

}
