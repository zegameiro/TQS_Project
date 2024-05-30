package deti.tqs.backend.integration.chair;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
class SearchChairTestsIT {
 
  private static final String BASE_URL = "http://localhost/api";

  @LocalServerPort
  private int port;

  private RoomRepository roomRepository;
  private ChairRepository chairRepository;

  private Chair c1 = new Chair();
  private Chair c2 = new Chair();
  private Chair c3 = new Chair();

  private Room r1 = new Room();
  private Room r2 = new Room();

  @Autowired
  SearchChairTestsIT(RoomRepository roomRepository, ChairRepository chairRepository) {
    this.roomRepository = roomRepository;
    this.chairRepository = chairRepository;
  }

  /*
   * NECESSARY TESTS
   * 
   *  1. Search for a chair that exists
   *  2. Search for a chair that does not exists
   *  3. Search for all the chairs that exists
   *  4. Search for all the chairs in a Room
   * 
  */

  @BeforeAll
  void setUp() {
    
    RestAssured.baseURI = BASE_URL;
    RestAssured.port = port;

    chairRepository.deleteAll();

    r1.setName("Room 11723");
    r1.setMaxChairsCapacity(10);
    
    r2.setName("Room 20192");
    r2.setMaxChairsCapacity(20);

    roomRepository.saveAllAndFlush(List.of(r1, r2));

    c1.setName("Chair 1293");
    c1.setRoom(r1);

    c2.setName("Chair 3248");
    c2.setRoom(r1);

    c3.setName("Chair 5934");
    c3.setRoom(r2);

    chairRepository.saveAllAndFlush(List.of(c1, c2, c3));

  }

  @Test
  @DisplayName("Search for a chair that exists")
  void searchForExistingChair() {
    
    Chair response = 
      given()
        .port(port)
      .when()
        .get("/chair/" + c1.getId())
      .then()
        .statusCode(200)
        .extract()
        .as(Chair.class);

    assertAll(
      () -> assertThat(response).isNotNull(),
      () -> assertThat(response.getName()).isEqualTo("Chair 1293")
    );

  }

  @Test
  @DisplayName("Search for a chair that does not exists")
  void searchForNonExistingChair() {
    
    given()
      .port(port)
    .when()
      .get("/chair/999999")
    .then()
      .statusCode(404);

  }

  @Test
  @DisplayName("Search for all the chairs that exists")
  void searchForAllChairs() {
    
    List<Chair> response = 
      given()
        .port(port)
      .when()
        .get("/chair/all")
      .then()
        .statusCode(200)
        .extract()
        .jsonPath()
        .getList(".", Chair.class);

    assertAll(
      () -> assertThat(response).isNotNull(),
      () -> assertThat(response).hasSize(3),
      () -> assertThat(response.get(0).getName()).isEqualTo("Chair 1293"),
      () -> assertThat(response.get(1).getName()).isEqualTo("Chair 3248"),
      () -> assertThat(response.get(2).getName()).isEqualTo("Chair 5934")
    );

  }

  @Test
  @DisplayName("Search for all the chairs in a Room")
  void searchForAllChairsInRoom() {
    
    List<Chair> response = 
      given()
        .port(port)
      .when()
        .get("/chair/all?roomID=" + r1.getId())
      .then()
        .statusCode(200)
        .extract()
        .jsonPath()
        .getList(".", Chair.class);

    assertAll(
      () -> assertThat(response).isNotNull(),
      () -> assertThat(response).hasSize(2),
      () -> assertThat(response.get(0).getName()).isEqualTo("Chair 1293"),
      () -> assertThat(response.get(1).getName()).isEqualTo("Chair 3248")
    );

  }

}
