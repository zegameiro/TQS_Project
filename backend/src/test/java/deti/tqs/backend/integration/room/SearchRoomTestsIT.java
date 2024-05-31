package deti.tqs.backend.integration.room;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.RoomRepository;
import io.restassured.RestAssured;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@TestInstance(Lifecycle.PER_CLASS)
class SearchRoomTestsIT {
  
  private final static String BASE_URL = "http://localhost/api";

  @LocalServerPort
  private int port;

  private FacilityRepository facilityRepository;
  
  private RoomRepository roomRepository;

  @Autowired
  SearchRoomTestsIT(FacilityRepository facilityRepository, RoomRepository roomRepository) {
    this.facilityRepository = facilityRepository;
    this.roomRepository = roomRepository;
  }

  private Facility f1 = new Facility();
  private Facility f2 = new Facility();

  private Room r1 = new Room();
  private Room r2 = new Room();
  private Room r3 = new Room();
  private Room r4 = new Room();

  /*
   * NECESSARY TESTS
   * 
   *  1. Search for a room with success with its ID
   *  2. Search for a room with an inexistent ID
   *  3. Search for all the room available
   *  4. Search for a room without a name or a facility ID
   *  5. Search for a room with a facility ID
   *  6. Search for a room with an inexistent facility ID
   *  7. Search for a room with a facility name
   *  8. Search for a room with an inexistent facility name
   *  9. Search for a room with a name and a facility ID
   * 10. Search for a room with a name or a facility ID that does not exist
   * 
  */

  @BeforeAll
  void setUp() {

    facilityRepository.deleteAll();
    roomRepository.deleteAll();

    RestAssured.baseURI = BASE_URL;
    RestAssured.port = port;

    f1.setName("Facility 1");
    f1.setCity("Aveiro");
    f1.setPhoneNumber("123456789");
    f1.setMaxRoomsCapacity(10);
    f1.setPostalCode("3810-193");
    f1.setStreetName("Rua de Aveiro");

    f2.setName("Facility 2");
    f2.setCity("Porto");
    f2.setPhoneNumber("987654321");
    f2.setMaxRoomsCapacity(20);
    f2.setPostalCode("1234-567");
    f2.setStreetName("Rua do Porto");

    facilityRepository.saveAllAndFlush(List.of(f1, f2));

    r1.setName("Room 1");
    r1.setMaxChairsCapacity(10);
    r1.setFacility(f1);
    r1.setBeautyServiceId(0);

    r2.setName("Room 2");
    r2.setMaxChairsCapacity(20);
    r2.setFacility(f1);
    r2.setBeautyServiceId(1);

    r3.setName("Room 3");
    r3.setMaxChairsCapacity(30);
    r3.setFacility(f2);
    r3.setBeautyServiceId(2);

    r4.setName("Room 4");
    r4.setMaxChairsCapacity(40);
    r4.setFacility(f2);
    r4.setBeautyServiceId(2);

    roomRepository.saveAllAndFlush(List.of(r1, r2, r3, r4));

  }

  @Test
  @DisplayName("Search for a room with success with its ID")
  void searchRoomWithSuccessWithID() {
    
    Room response = 
      given()
        .contentType("application/json")
        .port(port)
      .when()
        .get("/room/" + r1.getId())
      .then()
        .statusCode(200)
        .extract()
        .response()
        .as(Room.class);

    assertAll(
      () -> assertThat(response).isNotNull(),
      () -> assertThat(response.getName()).isEqualTo("Room 1"),
      () -> assertThat(response.getMaxChairsCapacity()).isEqualTo(10)
    );

  }

  @Test
  @DisplayName("Search for a room with an inexistent ID")
  void searchRoomWithInexistentID() {

    given()
      .port(port)
    .when()
      .get("/room/293")
    .then()
      .statusCode(404);

  }

  @Test
  @DisplayName("Search for all the room available")
  void searchAllRooms() {

    List<Room> response = 
      given()
        .contentType("application/json")
        .port(port)
      .when()
        .get("/room/all")
      .then()
        .statusCode(200)
        .extract()
        .body()
        .jsonPath()
        .getList(".", Room.class);

    assertAll(
      () -> assertThat(response).isNotNull(),
      () -> assertThat(response).hasSize(4),
      () -> assertThat(response.get(0).getName()).isEqualTo("Room 1"),
      () -> assertThat(response.get(0).getMaxChairsCapacity()).isEqualTo(10),
      () -> assertThat(response.get(1).getName()).isEqualTo("Room 2"),
      () -> assertThat(response.get(1).getMaxChairsCapacity()).isEqualTo(20),
      () -> assertThat(response.get(2).getName()).isEqualTo("Room 3"),
      () -> assertThat(response.get(2).getMaxChairsCapacity()).isEqualTo(30),
      () -> assertThat(response.get(3).getName()).isEqualTo("Room 4"),
      () -> assertThat(response.get(3).getMaxChairsCapacity()).isEqualTo(40)
    );

  }

  @Test
  @DisplayName("Search for a room without a name or a facility ID")
  void searchRoomWithoutNameOrFacilityID() {

    given()
      .contentType("application/json")
      .port(port)
    .when()
      .get("/room/search?facilityID=0")
    .then()
      .statusCode(400);

  }

  @Test
  @DisplayName("Search for a room with a facility ID")
  void searchRoomByFacilityID() {

    List<Room> response = 
      given()
        .contentType("application/json")
        .port(port)
      .when()
        .get("/room/search?facilityID=" + f1.getId())
      .then()
        .statusCode(200)
        .extract()
        .body()
        .jsonPath()
        .getList(".", Room.class);

    assertAll(
      () -> assertThat(response).isNotNull(),
      () -> assertThat(response).hasSize(2),
      () -> assertThat(response.get(0).getName()).isEqualTo("Room 1"),
      () -> assertThat(response.get(0).getMaxChairsCapacity()).isEqualTo(10),
      () -> assertThat(response.get(1).getName()).isEqualTo("Room 2"),
      () -> assertThat(response.get(1).getMaxChairsCapacity()).isEqualTo(20)
    );

  }

  @Test
  @DisplayName("Search for a room with an inexistent facility ID")
  void searchRoomByInexistentFacilityID() {

    given()
      .contentType("application/json")
      .port(port)
    .when()
      .get("/room/search?facilityID=1029")
    .then()
      .statusCode(404);

  }

  @Test
  @DisplayName("Search for a room with a facility name")
  void searchRoomByFacilityName() {

    List<Room> response = 
      given()
        .contentType("application/json")
        .port(port)
      .when()
        .get("/room/search?roomName=Facility 1&facilityID=0")
      .then()
        .statusCode(200)
        .extract()
        .body()
        .jsonPath()
        .getList(".", Room.class);

    assertAll(
      () -> assertThat(response).isNotNull(),
      () -> assertThat(response).hasSize(2),
      () -> assertThat(response.get(0).getName()).isEqualTo("Room 1"),
      () -> assertThat(response.get(0).getMaxChairsCapacity()).isEqualTo(10),
      () -> assertThat(response.get(1).getName()).isEqualTo("Room 2"),
      () -> assertThat(response.get(1).getMaxChairsCapacity()).isEqualTo(20)
    );

  }

  @Test
  @DisplayName("Search for a room with an inexistent facility name")
  void searchRoomByInexistentFacilityName() {

    given()
      .contentType("application/json")
      .port(port)
    .when()
      .get("/room/search?roomName=Facility&facilityID=0")
    .then()
      .statusCode(404);

  }

  @Test
  @DisplayName("Search for a room with a name and a facility ID")
  void searchRoomByNameAndFacilityID() {

    List<Room> response = 
      given()
        .contentType("application/json")
        .port(port)
      .when()
        .get("/room/search?roomName=Room 1&facilityID="+ f1.getId())
      .then()
        .statusCode(200)
        .extract()
        .body()
        .jsonPath()
        .getList(".", Room.class);

    assertAll(
      () -> assertThat(response).isNotNull(),
      () -> assertThat(response).hasSize(1),
      () -> assertThat(response.get(0).getName()).isEqualTo("Room 1"),
      () -> assertThat(response.get(0).getMaxChairsCapacity()).isEqualTo(10)
    );

  }

  @Test
  @DisplayName("Search for a room with a name or a facility ID that does not exist")
  void searchRoomByNameOrFacilityIDNotFound() {

    given()
      .contentType("application/json")
      .port(port)
    .when()
      .get("/room/search?roomName=Room 1029&facilityID=0")
    .then()
      .statusCode(404);

  }

}
