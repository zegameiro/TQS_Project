package deti.tqs.backend.services.chair;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
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
class DeleteChairServiceTests {

  @Mock
  private ChairRepository chairRepository;

  @InjectMocks
  private ChairService chairService;

  private Chair c1 = new Chair();
  private Chair c2 = new Chair();

  private Room r1 = new Room();

  /*
   * NECESSARY TESTS
   * 
   *  1. Delete a chair that exists
   *  2. Delete a chair that does not exists
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

  }

  @Test
  @DisplayName("Delete a chair that exists")
  void deleteChairThatExists() {

    when(chairRepository.findById(anyLong())).thenReturn(c1);
    when(chairRepository.findAll()).thenReturn(List.of(c2));

    chairService.deleteChair(1);

    List<Chair> chairs = chairRepository.findAll();

    assertThat(chairs).hasSize(1).doesNotContain(c1).contains(c2);

    verify(chairRepository, times(1)).findById(anyLong());
    verify(chairRepository, times(1)).delete(c1);

  }

  @Test
  @DisplayName("Delete a chair that does not exists")
  void deleteChairThatDoesNotExists() {

    when(chairRepository.findById(anyLong())).thenReturn(null);
    when(chairRepository.findAll()).thenReturn(List.of(c1, c2));

    assertThrows(EntityNotFoundException.class, () -> chairService.deleteChair(1294));

    List<Chair> chairs = chairRepository.findAll();

    assertThat(chairs).hasSize(2).contains(c1, c2);

    verify(chairRepository, times(1)).findById(anyLong());
    verify(chairRepository, times(0)).delete(c1);

  }

}
