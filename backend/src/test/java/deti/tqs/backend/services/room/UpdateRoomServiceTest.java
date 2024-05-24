package deti.tqs.backend.services.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
class UpdateRoomServiceTest {
  
  @Mock
  private RoomRepository roomRepository;

  @Mock
  private FacilityRepository facilityRepository;

  @InjectMocks
  private RoomService roomService;

  private Room r1 = new Room();
  private Room r2 = new Room();

  private Facility f1 = new Facility();
  private Facility f2 = new Facility();

  /*
   * NECESSARY TESTS
   * 
   *  1. Test update a room with success
   *  2. Test update a room that does not exist
   *  3. Test update a room with a name that already exists in the facility
   *  4. Test update a room with an invalid name
   *  5. Test update a room with an invalid capacity
   *  6. Test update a room with a facility that does not exist
   *  7. Test update a room with a facility at full capacity
   *  
  */

  @BeforeEach
  void setUp() {

    f1.setName("Facility 1");
    f1.setMaxRoomsCapacity(10);
    f1.setCity("Aveiro");
    f1.setPhoneNumber("123456789");
    f1.setPostalCode("3810-193");
    f1.setStreetName("Rua de Aveiro");

    f2.setName("Facility 2");
    f2.setMaxRoomsCapacity(1);
    f2.setCity("Porto");
    f2.setPhoneNumber("987654321");
    f2.setPostalCode("4000-193");
    f2.setStreetName("Rua do Porto");

    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);
    r1.setFacility(f1);

    r2.setName("Room 2");
    r2.setMaxChairsCapacity(15);
    r2.setFacility(f2);

  }

  @Test
  @DisplayName("Test update a room with success")
  void testUpdateRoomWithSuccess() throws NoSuchFieldException {
    
    when(roomRepository.findById(r1.getId())).thenReturn(r1);
    when(roomRepository.findByNameAndFacilityId(anyString(), anyLong())).thenReturn(null);

    Room r = new Room();
    r.setName("Room 1 Updated");
    r.setMaxChairsCapacity(20);
    r.setFacility(f2);

    when(roomRepository.save(any())).thenReturn(r);

    Room updatedRoom = roomService.updateRoom(r, r1.getId(), f2.getId());

    assertAll(
      () -> assertThat(updatedRoom).isNotNull(),
      () -> assertThat(updatedRoom.getName()).isEqualTo("Room 1 Updated"),
      () -> assertThat(updatedRoom.getMaxChairsCapacity()).isEqualTo(20),
      () -> assertThat(updatedRoom.getFacility()).isEqualTo(f2)
    );

    verify(roomRepository, times(1)).findById(anyLong());
    verify(roomRepository, times(1)).findByNameAndFacilityId(anyString(), anyLong());
    verify(roomRepository, times(1)).save(r1);

  }

  @Test
  @DisplayName("Test update a room that does not exist")
  void testUpdateRoomThatDoesNotExist() throws NoSuchFieldException {
    
    when(roomRepository.findById(anyLong())).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> roomService.updateRoom(r1, r1.getId(), f2.getId()));

    verify(roomRepository, times(1)).findById(anyLong());
    verify(roomRepository, never()).findByNameAndFacilityId(anyString(), anyLong());
    verify(facilityRepository, never()).findById(anyLong());
    verify(roomRepository, never()).save(any());

  }

  @Test
  @DisplayName("Test update a room with a name that already exists in the facility")
  void testUpdateRoomWithExistingName() throws NoSuchFieldException {
    
    when(roomRepository.findById(r1.getId())).thenReturn(r1);
    when(roomRepository.findByNameAndFacilityId(anyString(), anyLong())).thenReturn(r1);

    Room roomUpdate = new Room();
    roomUpdate.setName("Room 2");
    roomUpdate.setMaxChairsCapacity(20);
    roomUpdate.setFacility(f2);

    assertThrows(EntityExistsException.class, () -> roomService.updateRoom(roomUpdate, r1.getId(), f2.getId()));

    verify(roomRepository, times(1)).findById(anyLong());
    verify(roomRepository, times(1)).findByNameAndFacilityId(anyString(), anyLong());
    verify(roomRepository, never()).save(any());

  }

  @Test
  @DisplayName("Test update a room with an invalid name")
  void testUpdateRoomWithInvalidName() throws NoSuchFieldException {
    
    when(roomRepository.findById(r1.getId())).thenReturn(r1);

    r1.setName("");

    assertThrows(NoSuchFieldException.class, () -> roomService.updateRoom(r1, r1.getId(), f2.getId()));

    verify(roomRepository, times(1)).findById(anyLong());
    verify(roomRepository, never()).findByNameAndFacilityId(anyString(), anyLong());
    verify(roomRepository, never()).save(any());

  }

  @Test
  @DisplayName("Test update a room with an invalid capacity")
  void testUpdateRoomWithInvalidCapacity() throws NoSuchFieldException {
    
    when(roomRepository.findById(r1.getId())).thenReturn(r1);

    r1.setMaxChairsCapacity(0);

    assertThrows(NoSuchFieldException.class, () -> roomService.updateRoom(r1, r1.getId(), f2.getId()));

    verify(roomRepository, times(1)).findById(anyLong());
    verify(roomRepository, never()).findByNameAndFacilityId(anyString(), anyLong());
    verify(roomRepository, never()).save(any());

  }

  @Test
  @DisplayName("Test update a room with a facility that does not exist")
  void testUpdateRoomWithNonExistingFacility() throws NoSuchFieldException {
    
    when(roomRepository.findById(r1.getId())).thenReturn(r1);
    when(facilityRepository.findById(anyLong())).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> roomService.updateRoom(r1, r1.getId(), 1092L));

    verify(roomRepository, times(1)).findById(anyLong());
    verify(facilityRepository, times(1)).findById(anyLong());
    verify(roomRepository, never()).findByNameAndFacilityId(anyString(), anyLong());
    verify(roomRepository, never()).save(any());

  }

  @Test
  @DisplayName("Test update a room with a facility at full capacity")
  void testUpdateRoomWithFacilityAtFullCapacity() throws NoSuchFieldException {
    
    f2.setRooms(List.of(r2));

    when(roomRepository.findById(r1.getId())).thenReturn(r1);
    when(facilityRepository.findById(anyLong())).thenReturn(f2);

    assertThrows(IllegalStateException.class, () -> roomService.updateRoom(r1, r1.getId(), 2));

    verify(roomRepository, times(1)).findById(anyLong());
    verify(facilityRepository, times(1)).findById(anyLong());
    verify(roomRepository, never()).findByNameAndFacilityId(anyString(), anyLong());
    verify(roomRepository, never()).save(any());

  }

}
