package deti.tqs.backend.services.chair;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.ChairRepository;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.RoomRepository;
import deti.tqs.backend.services.ChairService;


@ExtendWith(MockitoExtension.class)
public class TestAddChairService {

    /* NECESSARY TESTS */
    /*
    * 1. Save a valid chair with success in existing room
    * 2. Fail to save an invalid chair in non existing room 
    */
    
    @Mock(lenient = true)
    private ChairRepository chairRepository;

    @Mock(lenient = true)
    private RoomRepository roomRepository;

    @Mock(lenient = true)
    private FacilityRepository facilityRepository;

    @InjectMocks
    private ChairService ChairService;

    private static Room room;
    private static Facility facility;


    @BeforeEach
    void setUp() {
        chairRepository.deleteAll();
        roomRepository.deleteAll();
        facilityRepository.deleteAll();

        facility = new Facility();
        facilityRepository.save(facility);

        room = new Room();
        room.setFacility(facility);
        roomRepository.save(room);
    }


    @Test
    @DisplayName("Test save a valid chair with success")
    public void testSaveChairInValidRoom() {
        
        Chair chair = new Chair();
        chair.setName("Good Chair");
        chair.setRoom(room);
        ChairService.addChair(chair);

        Mockito.when(chairRepository.save(chair)).thenReturn(chair);
    }

    @Test
    @DisplayName("Test fail to save an invalid chair in non existing room")
    public void testFailToSaveChairInInvalidRoom() {
        
        Room invalidRoom = new Room();

        Chair chair = new Chair();
        chair.setName("Bad Chair");
        chair.setRoom(invalidRoom);
        ChairService.addChair(chair);

        Mockito.when(chairRepository.save(chair)).thenReturn(null);
    }

}
