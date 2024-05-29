package deti.tqs.backend.integration.room;

import static io.restassured.RestAssured.given;

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

import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.RoomRepository;
import io.restassured.RestAssured;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteRoomTestsIT {
 
  private final static String BASE_URL = "http://localhost/api";

  private RoomRepository roomRepository;

  private FacilityRepository facilityRepository;

  @LocalServerPort
  private int port;

  @Autowired
  DeleteRoomTestsIT(RoomRepository roomRepository, FacilityRepository facilityRepository) {
    this.roomRepository = roomRepository;
    this.facilityRepository = facilityRepository;
  }

  /*
   * NECESSARY TESTS
   * 
   *  1. Delete a room with success
   *  2. Delete a room that does not exist
   * 
  */

  private Facility f = new Facility();

  private Room r = new Room();

  @BeforeAll
  void setUp() {

    RestAssured.baseURI = BASE_URL;
    RestAssured.port = port;

    f.setName("Facility 1");
    f.setCity("Aveiro");
    f.setPhoneNumber("123456789");
    f.setPostalCode("1234-567");
    f.setStreetName("Rua de Aveiro");
    f.setMaxRoomsCapacity(5);

    facilityRepository.saveAndFlush(f);

    r.setName("Room 1");
    r.setMaxChairsCapacity(5);
    r.setFacility(f);
    r.setBeautyServiceId(0);

    roomRepository.saveAndFlush(r);

  }

  @Test
  @DisplayName("Delete a room with success")
  void deleteRoomWithSuccess() {

    given()
      .contentType("application/json")
      .port(port)
    .when()
      .delete("/room/admin/delete?id=" + r.getId())
    .then()
      .statusCode(200);

  }

  @Test
  @DisplayName("Delete a room that does not exist")
  void deleteRoomThatDoesNotExist() {

    given()
      .contentType("application/json")
      .port(port)
    .when()
      .delete("/room/admin/delete?id=2")
    .then()
      .statusCode(404);

  }



}
