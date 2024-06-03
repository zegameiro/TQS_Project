package deti.tqs.backend.services.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class SearchRoomServiceTests {
  
  @Mock
  private RoomRepository roomRepository;

  @Mock
  private FacilityRepository facilityRepository;

  @InjectMocks
  private RoomService roomService;

  private Room r1 = new Room();
  private Room r2 = new Room();
  private Room r3 = new Room();
  
  private Facility f = new Facility();

  /*
   * NECESSARY TESTS
   * 
   *  1. Search for a room with an ID that exists
   *  2. Search for a room with an ID that does not exist
   *  3. Search for all rooms available
   *  4. Search for a room with a name
   *  5. Search for a room with a name that does not exist
   *  6. Search for a room with a facility ID
   *  7. Search for a room with a facility ID that does not exist
   *  8. Search for a room with a facility name
   *  9. Search for a room with a facility name that does not exist
   * 10. Search for a room without a name an facility ID
   * 11. Search for a room with a name and facility ID
   * 12. Search for a room with a name or facility ID that does not exist
   * 
  */

  @BeforeEach
  void setUp() {

    f.setName("Facility 1");
    f.setCity("Aveiro");
    f.setPhoneNumber("123456789");
    f.setPostalCode("3810-193");
    f.setStreetName("Rua de Aveiro");
    f.setMaxRoomsCapacity(5);

    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);
    r1.setFacility(f);
    r1.setBeautyServiceId(0);

    r2.setName("Room 2");
    r2.setMaxChairsCapacity(20);
    r2.setFacility(f);
    r2.setBeautyServiceId(1);

    r3.setName("Room 3");
    r3.setMaxChairsCapacity(30);
    r3.setFacility(f);
    r3.setBeautyServiceId(2);

  }

  @Test
  @DisplayName("Test search for a room with an ID that exists")
  void testSearchRoomById() {

    when(roomRepository.findById(anyLong())).thenReturn(r1);

    Room found = roomService.findById(1);

    assertAll(
      () -> assertThat(found).isNotNull(),
      () -> assertThat(found.getName()).isEqualTo("Room 1"),
      () -> assertThat(found.getMaxChairsCapacity()).isEqualTo(10),
      () -> assertThat(found.getFacility()).isEqualTo(f)
    );

  }

  @Test
  @DisplayName("Test search for a room with an ID that does not exist")
  void testSearchRoomByIdNotFound() {

    when(roomRepository.findById(anyLong())).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> roomService.findById(1029L));

  }

  @Test
  @DisplayName("Test search for all rooms available")
  void testSearchAllRooms() {

    when(roomRepository.findAll()).thenReturn(List.of(r1, r2, r3));

    assertAll(
      () -> assertThat(roomService.findAllRooms()).isNotNull(),
      () -> assertThat(roomService.findAllRooms()).hasSize(3),
      () -> assertThat(roomService.findAllRooms()).contains(r1, r2, r3)
    );

  }

  @Test
  @DisplayName("Test search for a room with a name")
  void testSearchRoomByName() {

    when(roomRepository.findByName(anyString())).thenReturn(r2);

    Room found = roomService.findByName("Room 2");

    assertAll(
      () -> assertThat(found).isNotNull(),
      () -> assertThat(found.getName()).isEqualTo("Room 2"),
      () -> assertThat(found.getMaxChairsCapacity()).isEqualTo(20),
      () -> assertThat(found.getFacility()).isEqualTo(f)
    );

  }

  @Test
  @DisplayName("Test search for a room with a name that does not exist")
  void testSearchRoomByNameNotFound() {

    when(roomRepository.findByName(anyString())).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> roomService.findByName("Room 1029"));

  }

  @Test
  @DisplayName("Test search for a room with a facility ID")
  void testSearchRoomByFacilityId() throws Exception {

    when(roomRepository.findByFacilityId(anyLong())).thenReturn(List.of(r1, r2, r3));

    assertThat(roomService.searchByFacilityInfo(null, 1)).isNotNull().hasSize(3).contains(r1,r2,r3);

  }

  @Test
  @DisplayName("Test search for rooms with a facility ID that does not exist")
  void testSearchRoomByFacilityIdNotFound() {

    when(roomRepository.findByFacilityId(anyLong())).thenReturn(List.of());

    assertThrows(EntityNotFoundException.class, () -> roomService.searchByFacilityInfo(null, 1029L));

  }

  @Test
  @DisplayName("Test search for a room with a facility name")
  void testSearchRoomByFacilityName() throws Exception {

    when(roomRepository.findByFacilityName(anyString())).thenReturn(List.of(r1, r2, r3));

    assertThat(roomService.searchByFacilityInfo("Facility 1", 0)).isNotNull().hasSize(3).contains(r1, r2, r3);

  }

  @Test
  @DisplayName("Test search for a room with a facility name that does not exist")
  void testSearchRoomByFacilityNameNotFound() {

    when(roomRepository.findByFacilityName(anyString())).thenReturn(List.of());

    assertThrows(EntityNotFoundException.class, () -> roomService.searchByFacilityInfo("Facility 1029", 0));

  }

  @Test
  @DisplayName("Test search for a room without a name or facility ID")
  void testSearchRoomByEmptyName() {

    assertThrows(NoSuchFieldException.class, () -> roomService.searchByFacilityInfo(null, 0));

  }

  @Test
  @DisplayName("Test search for a room with a name and facility ID")
  void testSearchRoomByNameAndFacilityId() throws Exception {

    when(roomRepository.findByNameAndFacilityId(anyString(), anyLong())).thenReturn(r3);

    List<Room> found = roomService.searchByFacilityInfo("Room 3", 1);

    assertAll(
      () -> assertThat(found).isNotNull(),
      () -> assertThat(found).hasSize(1),
      () -> assertThat(found.get(0).getName()).isEqualTo("Room 3"),
      () -> assertThat(found.get(0).getMaxChairsCapacity()).isEqualTo(30),
      () -> assertThat(found.get(0).getFacility()).isEqualTo(f)
    );

  }

  @Test
  @DisplayName("Test search for a room with a name or facility ID that does not exist")
  void testSearchRoomByNameAndFacilityIdNotFound() {

    when(roomRepository.findByNameAndFacilityId(anyString(), anyLong())).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> roomService.searchByFacilityInfo("Room 1029", 1029L));

  }

}
