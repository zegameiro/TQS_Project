package deti.tqs.backend.repositories.chair;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
class AddChairRepositoryTests {
    
    /* NECESSARY TESTS */
    /*
    * 1. Save a chair with success in existing room
    * 2. Save a chair with success without a room 
    * 3. New chair is available by default
    */

    private ChairRepository chairRepository;
    private RoomRepository roomRepository;

    @Autowired
    AddChairRepositoryTests(
        ChairRepository chairRepository, 
        RoomRepository roomRepository
    ) {
        this.chairRepository = chairRepository;
        this.roomRepository = roomRepository;
    }

    private Room room;

    @BeforeEach
    void setUp() {

        chairRepository.deleteAll();
        roomRepository.deleteAll();

        room = new Room();
        room.setName("Room 1");
        room.setMaxChairsCapacity(10);
        
        roomRepository.saveAndFlush(room);

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
            () -> assertThat(result.getName()).isEqualTo("Good Chair"),
            () -> assertThat(result.getRoom()).isEqualTo(room)
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
