package deti.tqs.backend.integration.room;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import deti.tqs.backend.JsonUtils;
import deti.tqs.backend.dtos.RoomSchema;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.repositories.FacilityRepository;
import io.restassured.RestAssured;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@TestInstance(Lifecycle.PER_CLASS)
class CreateRoomTestsIT {
  
  private final static String BASE_URL = "http://localhost/api";

  @LocalServerPort
  private int port;

  private FacilityRepository facilityRepository;

  @Autowired
  CreateRoomTestsIT(FacilityRepository facilityRepository) {
    this.facilityRepository = facilityRepository;
  }


  /*
   * NECESSARY TESTS
   * 
   *  1. Create a room with success
   *  2. Create a room with a name that already exists in a facility
   *  3. Create a room without a name
   *  4. Create a room with an invalid capacity
   *  5. Create a room to a facility that does not exists
   *  6. Create a room in a facility that already has the maximum number of rooms filled
   * 
  */

  @BeforeAll
  void setUp() {

    RestAssured.baseURI = BASE_URL;
    RestAssured.port = port;

    Facility f1 = new Facility();
    f1.setName("Facility 1");
    f1.setCity("Aveiro");
    f1.setId(1L);
    f1.setMaxRoomsCapacity(1);
    f1.setPhoneNumber("123456789");
    f1.setPostalCode("3810-193");
    f1.setStreetName("Rua de Aveiro");

    facilityRepository.save(f1);

  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("Create a room with success")
  void createRoomWithSuccess() throws IOException {

    RoomSchema rs1 = new RoomSchema(
      "Room 1",
      10,
      1L,
      "1"
    );

    HashMap<String, String> response = 
      given()
        .contentType("application/json")
        .port(port)
      .when()
        .body(JsonUtils.toJson(rs1))
        .post("/room/admin/add")
      .then()
        .statusCode(201)
        .extract()
        .as(HashMap.class);

    assertAll(
      () -> assertThat(response).isNotNull(),
      () -> assertThat(response).containsEntry("name", rs1.name())
    );

  }

  @Test
  @DisplayName("Create a room with a name that already exists in a facility")
  void createRoomWithExistingName() throws IOException {

    RoomSchema rs2 = new RoomSchema(
      "Room 1",
      29,
      1L,
      "0"
    );

    given()
      .contentType("application/json")
      .port(port)
    .when()
      .body(JsonUtils.toJson(rs2))
      .post("/room/admin/add")
    .then()
      .statusCode(409);

  }

  @Test
  @DisplayName("Create a room without a name")
  void createRoomWithoutName() throws IOException {

    RoomSchema rs3 = new RoomSchema(
      "",
      29,
      1L,
      "2"
    );

    given()
      .contentType("application/json")
      .port(port)
    .when()
      .body(JsonUtils.toJson(rs3))
      .post("/room/admin/add")
    .then()
      .statusCode(400);

  }

  @Test
  @DisplayName("Create a room with an invalid capacity")
  void createRoomWithInvalidCapacity() throws IOException {

    RoomSchema rs4 = new RoomSchema(
      "Room 2",
      -120,
      1L,
      "2"
    );

    given()
      .contentType("application/json")
      .port(port)
    .when()
      .body(JsonUtils.toJson(rs4))
      .post("/room/admin/add")
    .then()
      .statusCode(400);

  }

  @Test
  @DisplayName("Create a room to a facility that does not exists")
  void createRoomToNonExistingFacility() throws IOException {

    RoomSchema rs5 = new RoomSchema(
      "Room 3",
      29,
      140L,
      "2"
    );

    given()
      .contentType("application/json")
      .port(port)
    .when()
      .body(JsonUtils.toJson(rs5))
      .post("/room/admin/add")
    .then()
      .statusCode(404);

  }

  @Test
  @DisplayName("Create a room in a facility that already has the maximum number of rooms filled")
  void createRoomInFullFacility() throws IOException {

    RoomSchema rs6 = new RoomSchema(
      "Room 4",
      29,
      1L,
      "4"
    );

    given()
      .contentType("application/json")
      .port(port)
    .when()
      .body(JsonUtils.toJson(rs6))
      .post("/room/admin/add")
    .then()
      .statusCode(422);

  }

}
