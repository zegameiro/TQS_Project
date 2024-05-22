package deti.tqs.backend.repositories.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.RoomRepository;

@DataJpaTest
class AddRoomRepositoryTests {

  private RoomRepository roomRepository;
  private FacilityRepository facilityRepository;

  @Autowired
  AddRoomRepositoryTests(RoomRepository roomRepository, FacilityRepository facilityRepository) {
    this.roomRepository = roomRepository;
    this.facilityRepository = facilityRepository;
  }

  /*
   * NECESSARY TESTS
   * 
   *  1. Save a room with success and find it by id
   * 
  */

  @Test
  @DisplayName("Test save a room with success and find it by id")
  void testSaveRoomWithSuccess() {

    Facility f = new Facility();
    f.setName("Facility 1");
    f.setCity("Aveiro");
    f.setPhoneNumber("123456789");
    f.setPostalCode("3810-193");
    f.setStreetName("Rua de Aveiro");

    facilityRepository.save(f);

    Room r = new Room();
    r.setName("Room 1");
    r.setMaxChairsCapacity(10);
    r.setFacility(f);

    roomRepository.save(r);

    Room foundRoom = roomRepository.findById(r.getId());

    assertAll(
      () -> assertThat(foundRoom).isNotNull(),
      () -> assertThat(foundRoom.getName()).isEqualTo(r.getName()),
      () -> assertThat(foundRoom.getMaxChairsCapacity()).isEqualTo(r.getMaxChairsCapacity()),
      () -> assertThat(foundRoom.getFacility()).isEqualTo(r.getFacility())
    );

  }

}
