package deti.tqs.backend.services.chair;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.ChairRepository;
import deti.tqs.backend.repositories.RoomRepository;
import deti.tqs.backend.services.ChairService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class CreateChairServiceTests {
  
  @Mock
  private ChairRepository chairRepository;

  @Mock 
  private RoomRepository roomRepository;
  
  @InjectMocks
  private ChairService chairService;

  private Chair c1 = new Chair();
  private Chair c2 = new Chair();

  private Room r1 = new Room();
  private Room r2 = new Room();

  /*
   * NECESSARY TESTS
   * 
   *  1. Save a valid Chair
   *  2. Save a Chair with a name that already exists in a room
   *  3. Save a chair with an invalid name
   *  4. Save a chair to a room that does not exists
   *  5. Save a chair in a room that already has the maximum number of chairs filled
   * 
  */

  @BeforeEach
  void setUp() {

    r1.setId(1);
    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);

    r2.setId(2);
    r2.setName("Room 2");
    r2.setMaxChairsCapacity(1);

    c1.setId(1);
    c1.setName("Chair 1");
    c1.setRoom(r1);

    c2.setId(2);
    c2.setName("Chair 2");
    c2.setRoom(r2);

  }

  @Test
  @DisplayName("Save a valid Chair")
  void saveValidChair() throws Exception {

    when(roomRepository.findById(anyLong())).thenReturn(r1);
    when(chairRepository.findByNameAndRoomId(anyString(), anyLong())).thenReturn(null);
    when(chairRepository.save(c1)).thenReturn(c1);

    Chair savedChair = chairService.addChair(c1, r1.getId());

    assertAll(
      () -> assertThat(savedChair).isNotNull(),
      () -> assertThat(savedChair.getName()).isEqualTo("Chair 1"),
      () -> assertThat(savedChair.getRoom()).isEqualTo(r1),
      () -> assertTrue(savedChair.isAvailable())
    );

    verify(roomRepository, times(1)).findById(anyLong());
    verify(chairRepository, times(1)).findByNameAndRoomId(anyString(), anyLong());
    verify(chairRepository, times(1)).save(c1);

  }

  @Test
  @DisplayName("Save a Chair with a name that already exists in a room")
  void saveChairWithSameName() throws Exception {

    when(roomRepository.findById(anyLong())).thenReturn(r1);
    when(chairRepository.findByNameAndRoomId(anyString(), anyLong())).thenReturn(c1);

    assertThrows(EntityExistsException.class, () -> chairService.addChair(c2, 1));

    verify(roomRepository, times(1)).findById(anyLong());
    verify(chairRepository, times(1)).findByNameAndRoomId(anyString(), anyLong());
    verify(chairRepository, never()).save(c2);

  }

  @Test
  @DisplayName("Save a chair with an invalid name")
  void saveChairWithInvalidName() throws Exception {

    c1.setName("");

    assertThrows(NoSuchFieldException.class, () -> chairService.addChair(c1, 1));

    verify(roomRepository, never()).findById(anyLong());
    verify(chairRepository, never()).findByNameAndRoomId(anyString(), anyLong());
    verify(chairRepository, never()).save(c1);

  }

  @Test
  @DisplayName("Save a chair with an null name")
  void saveChairWithNullName() throws Exception {

    c1.setName(null);

    assertThrows(NoSuchFieldException.class, () -> chairService.addChair(c1, 1));

    verify(roomRepository, never()).findById(anyLong());
    verify(chairRepository, never()).findByNameAndRoomId(anyString(), anyLong());
    verify(chairRepository, never()).save(c1);

  }

  @Test
  @DisplayName("Save a chair to a room that does not exists")
  void saveChairToNonExistingRoom() {

    when(roomRepository.findById(anyLong())).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> chairService.addChair(c1, 1));

    verify(roomRepository, times(1)).findById(anyLong());
    verify(chairRepository, never()).findByNameAndRoomId(anyString(), anyLong());
    verify(chairRepository, never()).save(any());

  }

  @Test
  @DisplayName("Save a chair in a room that already has the maximum number of chairs filled")
  void saveChairInFullRoom() throws Exception {

    r2.setChairs(List.of(c2));

    when(roomRepository.findById(anyLong())).thenReturn(r2);

    long roomID = r2.getId();

    assertThrows(IllegalStateException.class, () -> chairService.addChair(c2, roomID));

    verify(roomRepository, times(1)).findById(anyLong());
    verify(chairRepository, never()).findByNameAndRoomId(anyString(), anyLong());
    verify(chairRepository, never()).save(c2);

  }

}
