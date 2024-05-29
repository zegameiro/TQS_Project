package deti.tqs.backend.integration.chair;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import deti.tqs.backend.JsonUtils;
import deti.tqs.backend.dtos.ChairSchema;
import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.RoomRepository;
import io.restassured.RestAssured;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@TestInstance(Lifecycle.PER_CLASS)
class CreateChairTestsIT {
  
  private static final String BASE_URL = "http://localhost/api";

  @LocalServerPort
  private int port;

  private RoomRepository roomRepository;

  @Autowired
  CreateChairTestsIT(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  private Room r1 = new Room();
  private Room r2 = new Room();

  /*
   * NECESSARY TESTS
   * 
   *  1. Create a chair with success
   *  2. Create a chair with a name that already exists in a room
   *  3. Create a chair with an invalid name
   *  4. Create a chair to a room that does not exists
   *  5. Create a chair in a room that already has the maximum number of chairs filled
   * 
  */

  @BeforeAll
  void setUp() {

    RestAssured.baseURI = BASE_URL;
    RestAssured.port = port;

    r1.setName("Room 672");
    r1.setMaxChairsCapacity(10);

    r2.setName("Room 1283");
    r2.setMaxChairsCapacity(1);

    roomRepository.saveAllAndFlush(List.of(r1, r2));

  }

  @Test
  @DisplayName("Create a chair with success")
  void createChairWithSuccess() throws IOException {
  
    ChairSchema cs = new ChairSchema(
      "Chair 1",
      r1.getId()
    );

    Chair response = 
      given()
        .contentType("application/json")
        .port(port)
        .body(JsonUtils.toJson(cs))
      .when()
        .post("/chair/admin/add")
      .then()
        .statusCode(201)
        .extract()
        .as(Chair.class);

    assertAll(
      () -> assertThat(response).isNotNull(),
      () -> assertThat(response.getName()).isEqualTo("Chair 1"),
      () -> assertTrue(response.isAvailable())
    );

  }

  @Test
  @DisplayName("Create a chair with a name that already exists in a room")
  void createChairWithExistingName() throws IOException {
    
    ChairSchema cs1 = new ChairSchema(
      "Chair 2",
      r1.getId()
    );

    given()
      .contentType("application/json")
      .port(port)
      .body(JsonUtils.toJson(cs1))
    .when()
      .post("/chair/admin/add")
    .then()
      .statusCode(201);

    ChairSchema cs2 = new ChairSchema(
      "Chair 2",
      r1.getId()
    );

    given()
      .contentType("application/json")
      .port(port)
      .body(JsonUtils.toJson(cs2))
    .when()
      .post("/chair/admin/add")
    .then()
      .statusCode(409);

  }

  @Test
  @DisplayName("Create a chair with an invalid name")
  void createChairWithInvalidName() throws IOException {

    ChairSchema cs = new ChairSchema(
      "",
      r1.getId()
    );

    given()
      .contentType("application/json")
      .port(port)
      .body(JsonUtils.toJson(cs))
    .when()
      .post("/chair/admin/add")
    .then()
      .statusCode(400);

  }

  @Test
  @DisplayName("Create a chair to a room that does not exists")
  void createChairToNonExistingRoom() throws IOException {

    ChairSchema cs = new ChairSchema(
      "Chair 3",
      11245L
    );

    given()
      .contentType("application/json")
      .port(port)
      .body(JsonUtils.toJson(cs))
    .when()
      .post("/chair/admin/add")
    .then()
      .statusCode(404);

  }

  @Test
  @DisplayName("Create a chair in a room that already has the maximum number of chairs filled")
  void createChairInFullRoom() throws IOException {

    ChairSchema cs1 = new ChairSchema(
      "Chair 4",
      r2.getId()
    );

    given()
      .contentType("application/json")
      .port(port)
      .body(JsonUtils.toJson(cs1))
    .when()
      .post("/chair/admin/add")
    .then()
      .statusCode(201);

    ChairSchema cs2 = new ChairSchema(
      "Chair 5",
      r2.getId()
    );

    given()
      .contentType("application/json")
      .port(port)
      .body(JsonUtils.toJson(cs2))
    .when()
      .post("/chair/admin/add")
    .then()
      .statusCode(422);

  }

}