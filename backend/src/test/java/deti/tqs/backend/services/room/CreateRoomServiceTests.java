package deti.tqs.backend.services.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.RoomRepository;
import deti.tqs.backend.services.RoomService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class CreateRoomServiceTests {
  
  @Mock
  private RoomRepository roomRepository;

  @Mock
  private FacilityRepository facilityRepository;

  @InjectMocks
  private RoomService roomService;
  

  private Room r1 = new Room();
  private Room r2 = new Room();
  private Room r3 = new Room();

  private Facility f1 = new Facility();
  private Facility f2 = new Facility();

  /*
   * NECESSARY TESTS
   * 
   *  1. Save a valid Room
   *  2. Save a Room with a name that already exists in a facility
   *  3. Save a facility with missing fields
   *  4. Save a room to a facility that does not exists
   *  5. Save a room with an invalid chairs capacity
   *  6. Save a room in a facility that already has the maximum number of rooms filled
   * 
  */

  @BeforeEach
  void setUp() {

    f1.setName("Facility 1");
    f1.setCity("Aveiro");
    f1.setPhoneNumber("123456789");
    f1.setPostalCode("3810-193");
    f1.setStreetName("Rua de Aveiro");
    f1.setMaxRoomsCapacity(5);

    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);
    r1.setFacility(f1);

    f2.setName("Facility 2");
    f2.setCity("Porto");
    f2.setPhoneNumber("987654321");
    f2.setPostalCode("4000-007");
    f2.setStreetName("Rua do Porto");

    r2.setMaxChairsCapacity(20);
    r2.setFacility(f2);
    r2.setMaxChairsCapacity(1);

    r3.setName("Room 3");
    r3.setMaxChairsCapacity(30);
    r3.setFacility(f1);

  }

  @Test
  @DisplayName("When saving a valid Room, it should return the saved Room")
  void saveValidRoom() throws NoSuchFieldException {
    
    when(facilityRepository.findById(anyLong())).thenReturn(f1);
    when(roomRepository.findByNameAndFacilityId(anyString(), anyLong())).thenReturn(null);
    when(roomRepository.save(any())).thenReturn(r1);

    Room savedRoom = roomService.save(r1, f1.getId());
    
    assertThat(savedRoom).isNotNull().isEqualTo(r1);

    verify(facilityRepository, times(1)).findById(anyLong());
    verify(roomRepository, times(1)).save(any());

  }

  @Test
  @DisplayName("When saving a Room with a name that already exists in a facility, it should return null")
  void saveRoomWithExistingName() {
    
    when(facilityRepository.findById(anyLong())).thenReturn(f1);
    when(roomRepository.findByNameAndFacilityId(anyString(), anyLong())).thenReturn(r1);
    
    assertThatThrownBy(() -> roomService.save(r3, f1.getId()))
      .isInstanceOf(EntityExistsException.class)
      .hasMessage("Room with this name already exists in this facility");

    verify(facilityRepository, times(1)).findById(anyLong());
    verify(roomRepository, times(1)).findByNameAndFacilityId(anyString(), anyLong());
    verify(roomRepository, times(0)).save(any());
  }

  @Test
  @DisplayName("When saving a Room with missing fields, it should return null")
  void saveRoomWithMissingFields() throws NoSuchFieldException {
    
    assertThatThrownBy(() -> roomService.save(r2, f2.getId()))
      .isInstanceOf(NoSuchFieldException.class)
      .hasMessage("Room must have a name");

    verify(facilityRepository, times(0)).findById(anyLong());
    verify(roomRepository, times(0)).findByNameAndFacilityId(anyString(), anyLong());
    verify(roomRepository, times(0)).save(any());

  }

  @Test
  @DisplayName("When saving a Room to a facility that does not exists, it should return null")
  void saveRoomToNonExistingFacility() {
    
    when(facilityRepository.findById(anyLong())).thenReturn(null);
    
    assertThatThrownBy(() -> roomService.save(r1, f1.getId()))
      .isInstanceOf(EntityNotFoundException.class)
      .hasMessage("Facility does not exist");

    verify(facilityRepository, times(1)).findById(anyLong());
    verify(roomRepository, times(0)).findByNameAndFacilityId(anyString(), anyLong());
    verify(roomRepository, times(0)).save(any());

  }

  @Test
  @DisplayName("When saving a Room with an invalid chairs capacity, it should return null")
  void saveRoomWithInvalidChairsCapacity() throws NoSuchFieldException {
    
    r2.setName("Room 2");
    r2.setMaxChairsCapacity(0);

    assertThatThrownBy(() -> roomService.save(r2, f1.getId()))
      .isInstanceOf(NoSuchFieldException.class)
      .hasMessage("Room must have a valid capacity value greater than 0");

    verify(facilityRepository, times(0)).findById(anyLong());
    verify(roomRepository, times(0)).findByNameAndFacilityId(anyString(), anyLong());
    verify(roomRepository, times(0)).save(any());

  }

  @Test
  @DisplayName("When saving a Room in a facility that already has the maximum number of rooms filled, it should return null")
  void saveRoomInFullCapacityFacility() throws NoSuchFieldException {
    
    f1.setMaxRoomsCapacity(1);

    when(facilityRepository.findById(anyLong())).thenReturn(f1);
    when(roomRepository.findByNameAndFacilityId(anyString(), anyLong())).thenReturn(null);

    f1.setRooms(List.of(r1));

    assertThatThrownBy(() -> roomService.save(r3, f1.getId()))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Facility is at full capacity");

    verify(facilityRepository, times(1)).findById(anyLong());
    verify(roomRepository, times(1)).findByNameAndFacilityId(anyString(), anyLong());
    verify(roomRepository, never()).save(any());
  
  }

}
