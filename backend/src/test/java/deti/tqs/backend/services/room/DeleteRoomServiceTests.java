package deti.tqs.backend.services.room;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.RoomRepository;
import deti.tqs.backend.services.RoomService;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class DeleteRoomServiceTests {

  @Mock
  private RoomRepository roomRepository;

  @InjectMocks
  private RoomService roomService;

  private Room r1 = new Room();
  private Facility f = new Facility();

  @BeforeEach
  void setUp() {

    f.setId(1L);
    f.setName("Facility 1");
    f.setCity("Aveiro");
    f.setMaxRoomsCapacity(10);
    f.setPhoneNumber("123456789");
    f.setPostalCode("3810-193");
    f.setStreetName("Rua de Aveiro");

    r1.setId(1L);
    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);
    r1.setFacility(f);

  }

  /*
   * NECESSARY TESTS
   * 
   *  1. Delete a room with success
   *  2. Delete a room that does not exist
   * 
  */

  @Test
  @DisplayName("Test delete a room with success")
  void testDeleteRoomWithSuccess() {

    when(roomRepository.findById(anyLong())).thenReturn(r1);

    roomService.deleteRoom(r1.getId());

    List<Room> deletedRoom = roomRepository.findAll();

    assertThat(deletedRoom).isEmpty();

    verify(roomRepository, times(1)).findById(anyLong());
    verify(roomRepository, times(1)).delete(any());

  }

  @Test
  @DisplayName("Test delete a room that does not exist")
  void testDeleteRoomThatDoesNotExist() {

    when(roomRepository.findById(anyLong())).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> roomService.deleteRoom(r1.getId()));

    verify(roomRepository, times(1)).findById(anyLong());
    verify(roomRepository, times(0)).delete(any());

  }
  
}
