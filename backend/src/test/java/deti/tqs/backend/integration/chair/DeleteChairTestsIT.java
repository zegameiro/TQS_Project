package deti.tqs.backend.integration.chair;

import static io.restassured.RestAssured.given;
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

import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.ChairRepository;
import deti.tqs.backend.repositories.RoomRepository;
import io.restassured.RestAssured;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@TestInstance(Lifecycle.PER_CLASS)
class DeleteChairTestsIT {
  
  private static final String BASE_URL = "http://localhost/api";

  @LocalServerPort
  private int port;

  private RoomRepository roomRepository;
  private ChairRepository chairRepository;

  private Chair c1 = new Chair();
  private Chair c2 = new Chair();

  private Room r1 = new Room();

  @Autowired
  DeleteChairTestsIT(RoomRepository roomRepository, ChairRepository chairRepository) {
    this.roomRepository = roomRepository;
    this.chairRepository = chairRepository;
  }

  /*
   * NECESSARY TESTS
   * 
   *  1. Delete a chair that exists successfully
   *  2. Delete a chair that does not exists
   * 
  */

  @BeforeAll
  void setUp() {
    
    RestAssured.baseURI = BASE_URL;
    RestAssured.port = port;

    chairRepository.deleteAll();

    r1.setName("Room UHF");
    r1.setMaxChairsCapacity(10);

    roomRepository.saveAndFlush(r1);

    c1.setName("Chair GDR");
    c1.setRoom(r1);

    c2.setName("Chair FFOQ");
    c2.setRoom(r1);

    chairRepository.saveAllAndFlush(List.of(c1, c2));

  }

  @Test
  @DisplayName("Delete a chair that exists successfully")
  void deleteChairThatExists() {
    
    given()
      .contentType("application/json")
      .port(port)
    .when()
      .delete("/chair/admin/delete?id=" + c1.getId())
    .then()
      .statusCode(200);

    List<Chair> foundChairs = 
      given()
        .contentType("application/json")
        .port(port)
      .when()
        .get("/chair/all")
      .then()
        .statusCode(200)
        .extract()
        .jsonPath()
        .getList(".", Chair.class);

    assertThat(foundChairs).hasSize(1);

  }

  @Test
  @DisplayName("Delete a chair that does not exists")
  void deleteChairThatDoesNotExists() {
    
    given()
      .contentType("application/json")
      .port(port)
    .when()
      .delete("/chair/admin/delete?id=1283")
    .then()
        .statusCode(404);

  }

}
