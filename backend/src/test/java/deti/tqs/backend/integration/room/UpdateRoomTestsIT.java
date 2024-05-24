package deti.tqs.backend.integration.room;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;

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

import deti.tqs.backend.JsonUtils;
import deti.tqs.backend.dtos.RoomSchema;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.RoomRepository;
import io.restassured.RestAssured;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@TestInstance(Lifecycle.PER_CLASS)
class UpdateRoomTestsIT {
  
  private static final String BASE_URL = "http://localhost/api";

  @LocalServerPort
  private int port;

  private FacilityRepository facilityRepository;

  private RoomRepository roomRepository;

  @Autowired
  UpdateRoomTestsIT(FacilityRepository facilityRepository, RoomRepository roomRepository) {
    this.facilityRepository = facilityRepository;
    this.roomRepository = roomRepository;
  }

  private Facility f1 = new Facility();
  private Facility f2 = new Facility();

  private Room r1 = new Room();
  private Room r2 = new Room();

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

  @BeforeAll
  void setUp() {

    RestAssured.baseURI = BASE_URL;
    RestAssured.port = port;

    f1.setName("Facility ABCD");
    f1.setMaxRoomsCapacity(1);
    f1.setCity("Aveiro");
    f1.setPhoneNumber("123456789");
    f1.setPostalCode("3810-193");
    f1.setStreetName("Rua de Aveiro");

    f2.setName("Facility EFGHI");
    f2.setMaxRoomsCapacity(10);
    f2.setCity("Porto");
    f2.setPhoneNumber("987654321");
    f2.setPostalCode("4000-193");
    f2.setStreetName("Rua do Porto");

    facilityRepository.saveAndFlush(f1);
    facilityRepository.saveAndFlush(f2);

    r1.setName("Room 1");
    r1.setMaxChairsCapacity(20);
    r1.setFacility(f1);

    r2.setName("Room 2");
    r2.setMaxChairsCapacity(30);
    r2.setFacility(f2);

    roomRepository.saveAndFlush(r1);
    roomRepository.saveAndFlush(r2);

  }

  @Test
  @DisplayName("Test update a room with success")
  void testUpdateRoomSuccess() throws IOException {

    RoomSchema roomSchema = new RoomSchema(
      "Room 3",
      2,
      f2.getId()
    );
    
    Room updatedRoom = 
      given()
        .contentType("application/json")
        .body(JsonUtils.toJson(roomSchema))
      .when()
        .put("/room/admin/update?id=" + r2.getId())
      .then()
        .statusCode(200)
        .extract()
        .response()
        .as(Room.class);
    
    assertAll(
      () -> assertThat(updatedRoom).isNotNull(),
      () -> assertThat(updatedRoom.getName()).isEqualTo("Room 3"),
      () -> assertThat(updatedRoom.getMaxChairsCapacity()).isEqualTo(2)
    );

  }

  @Test
  @DisplayName("Test update a room that does not exist")
  void testUpdateRoomDoesNotExist() throws IOException {

    RoomSchema roomSchema = new RoomSchema(
      "Room 3",
      2,
      f1.getId()
    );
    
    given()
      .contentType("application/json")
      .body(JsonUtils.toJson(roomSchema))
    .when()
      .put("/room/admin/update?id=23945")
    .then()
      .statusCode(404);

  }

  @Test
  @DisplayName("Test update a room with a name that already exists in the facility")
  void testUpdateRoomNameAlreadyExists() throws IOException {

    RoomSchema roomSchema = new RoomSchema(
      "Room 2",
      2,
      f2.getId()
    );
    
    given()
      .contentType("application/json")
      .body(JsonUtils.toJson(roomSchema))
    .when()
      .put("/room/admin/update?id=" + r1.getId())
    .then()
      .statusCode(409);

  }

  @Test
  @DisplayName("Test update a room with an invalid name")
  void testUpdateRoomInvalidName() throws IOException {

    RoomSchema roomSchema = new RoomSchema(
      "",
      2,
      f1.getId()
    );
    
    given()
      .contentType("application/json")
      .body(JsonUtils.toJson(roomSchema))
    .when()
      .put("/room/admin/update?id=" + r1.getId())
    .then()
      .statusCode(400);

  }

  @Test
  @DisplayName("Test update a room with an invalid capacity")
  void testUpdateRoomInvalidCapacity() throws IOException {

    RoomSchema roomSchema = new RoomSchema(
      "Room 3",
      -1,
      f1.getId()
    );
    
    given()
      .contentType("application/json")
      .body(JsonUtils.toJson(roomSchema))
    .when()
      .put("/room/admin/update?id=" + r1.getId())
    .then()
      .statusCode(400);

  }

  @Test
  @DisplayName("Test update a room with a facility that does not exist")
  void testUpdateRoomFacilityDoesNotExist() throws IOException {

    RoomSchema roomSchema = new RoomSchema(
      "Room 3",
      2,
      23945
    );
    
    given()
      .contentType("application/json")
      .body(JsonUtils.toJson(roomSchema))
    .when()
      .put("/room/admin/update?id=" + r1.getId())
    .then()
      .statusCode(404);

  }

  @Test
  @DisplayName("Test update a room with a facility at full capacity")
  void testUpdateRoomFacilityFullCapacity() throws IOException {

    RoomSchema roomSchema = new RoomSchema(
      "Room 3",
      2,
      f1.getId()
    );
    
    given()
      .contentType("application/json")
      .body(JsonUtils.toJson(roomSchema))
    .when()
      .put("/room/admin/update?id=" + r2.getId())
    .then()
      .statusCode(422);

  }

}
