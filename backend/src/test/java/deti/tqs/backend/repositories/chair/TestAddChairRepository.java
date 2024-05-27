package deti.tqs.backend.repositories.chair;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.ChairRepository;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.RoomRepository;

@DataJpaTest
class TestAddChairRepository {
    
    /* NECESSARY TESTS */
    /*
    * 1. Save a chair with success in existing room
    * 2. Save a chair with success without a room 
    * 3. New chair is available by default
    */

    private ChairRepository chairRepository;
    private RoomRepository roomRepository;
    private FacilityRepository facilityRepository;

    @Autowired
    TestAddChairRepository(
        ChairRepository chairRepository, 
        RoomRepository roomRepository,
        FacilityRepository facilityRepository
    ) {
        this.chairRepository = chairRepository;
        this.roomRepository = roomRepository;
        this.facilityRepository = facilityRepository;
    }

    private static Room room;

    @BeforeEach
    void setUp() {
        chairRepository.deleteAll();
        roomRepository.deleteAll();
        facilityRepository.deleteAll();        

        Facility facility = new Facility();
        facility.setName("Facility");
        facility.setCity("Aveiro");
        facility.setStreetName("Street");
        facility.setPostalCode("1234-567");
        facility.setPhoneNumber("123456789");
        facility.setMaxRoomsCapacity(10);
        facilityRepository.save(facility);

        room = new Room();
        room.setName("Room");
        room.setMaxChairsCapacity(5);
        room.setFacility(facility);
        roomRepository.save(room);
    }


    @Test
    @DisplayName("Test save a chair with success")
    void testSaveChairWithSuccess() {

        Chair chair = new Chair();
        chair.setName("Good Chair");
        chair.setRoom(room);
        chairRepository.save(chair);

        Chair result = chairRepository.findById(chair.getId());
        assertAll(
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result.getName()).isEqualTo(chair.getName()),
            () -> assertThat(result.getRoom()).isEqualTo(chair.getRoom())
        );
    }

    @Test
    @DisplayName("Save a chair with success without a room")
    void testFailToSaveChairInNonExistingRoom() {
        Chair chair = new Chair();
        chair.setName("Bad Chair");
        chair.setRoom(null);
        chairRepository.save(chair);

        Chair result = chairRepository.findById(chair.getId());
        assertAll(
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result.getName()).isEqualTo(chair.getName()),
            () -> assertThat(result.getRoom()).isNull()
        );
    }

    @Test
    @DisplayName("New chair is available by default")
    void testNewChairIsAvailableByDefault() {
        Chair chair = new Chair();
        chair.setName("New Chair");
        chair.setRoom(room);
        chairRepository.save(chair);

        Chair result = chairRepository.findById(chair.getId());
        assertThat(result.isAvailable()).isTrue();
    }
}
