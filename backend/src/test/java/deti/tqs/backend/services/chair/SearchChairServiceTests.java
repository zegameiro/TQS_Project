package deti.tqs.backend.services.chair;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import deti.tqs.backend.services.ChairService;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class SearchChairServiceTests {
  
  @Mock
  private ChairRepository chairRepository;

  @InjectMocks
  private ChairService chairService;

  private Chair c1 = new Chair();
  private Chair c2 = new Chair();
  private Chair c3 = new Chair();
  private Chair c4 = new Chair();

  private Room r1 = new Room();

  /*
   * NECESSARY TESTS
   * 
   *  1. Search for a chair that exists
   *  2. Search for a chair that does not exists
   *  3. Search for a chair by its name
   *  4. Search for a chair by a name that does not exists
   *  5. Search for a chair by its name and room
   *  6. Search for a chair by a name that does not exists in a room
   *  7. Get all the chairs
   *  8. Get all the chairs in a room
   * 
  */

  @BeforeEach
  void setUp() {

    r1.setId(1);
    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);

    c1.setId(1);
    c1.setName("Chair 1");
    c1.setRoom(r1);

    c2.setId(2);
    c2.setName("Chair 2");
    c2.setRoom(r1);

    c3.setId(3);
    c3.setName("Chair 3");
    c3.setRoom(r1);

    c4.setId(4);
    c4.setName("Chair 4");
    c4.setRoom(r1);

  }

  @Test
  @DisplayName("Search for a chair that exists")
  void searchForExistingChair() {
    
    when(chairRepository.findById(anyLong())).thenReturn(c1);

    Chair foundChair = chairService.getChair(c1.getId());

    assertAll(
      () -> assertThat(foundChair).isNotNull(),
      () -> assertThat(foundChair).isEqualTo(c1)
    );

    verify(chairRepository, times(1)).findById(anyLong());

  }

  @Test
  @DisplayName("Search for a chair that does not exists")
  void searchForNonExistingChair() {
    
    when(chairRepository.findById(anyLong())).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> chairService.getChair(c1.getId()));

    verify(chairRepository, times(1)).findById(anyLong());

  }

  @Test
  @DisplayName("Search for a chair by its name")
  void searchForChairByName() {
    
    when(chairRepository.findByName(anyString())).thenReturn(c2);

    Chair foundChair = chairService.getChairByName(c2.getName());

    assertAll(
      () -> assertThat(foundChair).isNotNull(),
      () -> assertThat(foundChair).isEqualTo(c2)
    );

    verify(chairRepository, times(1)).findByName(anyString());

  }

  @Test
  @DisplayName("Search for a chair by a name that does not exists")
  void searchForNonExistingChairByName() {
    
    when(chairRepository.findByName(anyString())).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> chairService.getChairByName("Chair ABDHC"));

    verify(chairRepository, times(1)).findByName(anyString());

  }

  @Test
  @DisplayName("Search for a chair by its name and room")
  void searchForChairByNameAndRoom() {
    
    when(chairRepository.findByNameAndRoomId(anyString(), anyLong())).thenReturn(c3);

    Chair foundChair = chairService.getChairByNameAndRoomID(c3.getName(), c3.getRoom().getId());

    assertAll(
      () -> assertThat(foundChair).isNotNull(),
      () -> assertThat(foundChair).isEqualTo(c3)
    );

    verify(chairRepository, times(1)).findByNameAndRoomId(anyString(), anyLong());

  }

  @Test
  @DisplayName("Search for a chair by a name that does not exists in a room")
  void searchForNonExistingChairByNameAndRoom() {
    
    when(chairRepository.findByNameAndRoomId(anyString(), anyLong())).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> chairService.getChairByNameAndRoomID("Chair ABDHC", 1));

    verify(chairRepository, times(1)).findByNameAndRoomId(anyString(), anyLong());

  }
  
  @Test
  @DisplayName("Get all the chairs")
  void getAllChairs() {
    
    when(chairRepository.findAll()).thenReturn(List.of(c1, c2, c3, c4));

    List<Chair> chairs = chairService.getAllChairs();

    assertAll(
      () -> assertThat(chairs).isNotNull(),
      () -> assertThat(chairs.size()).isEqualTo(4),
      () -> assertThat(chairs).contains(c1, c2, c3, c4)
    );

    verify(chairRepository, times(1)).findAll();

  }

  @Test
  @DisplayName("Get all the chairs in a room")
  void getAllChairsInRoom() {
    
    when(chairRepository.findByRoomId(anyLong())).thenReturn(List.of(c1, c2, c3, c4));

    List<Chair> chairs = chairService.getChairsByRoomID(r1.getId());

    assertAll(
      () -> assertThat(chairs).isNotNull(),
      () -> assertThat(chairs.size()).isEqualTo(4),
      () -> assertThat(chairs).contains(c1, c2, c3, c4)
    );

    verify(chairRepository, times(1)).findByRoomId(anyLong());

  }

}
