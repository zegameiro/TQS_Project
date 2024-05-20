package deti.tqs.backend.integration.facility;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import deti.tqs.backend.JsonUtils;
import deti.tqs.backend.models.Facility;
import io.restassured.RestAssured;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@TestInstance(Lifecycle.PER_CLASS)
class DeleteFacilityTestsIT {
  
  private final static String BASE_URL = "http://localhost/api";


  @LocalServerPort
  private int port;

  /*
   * NECESSARY TESTS
   * 
   *  1. Delete a facility with success
   *  2. Delete a facility that does not exist
   * 
   */

  @BeforeAll
  void setUp() {

    RestAssured.baseURI = BASE_URL;
    RestAssured.port = port;

  }

  @Test
  @DisplayName("Delete a facility with success")
  void deleteFacilityWithSuccess() throws IOException {

    Facility f = new Facility();
    f.setId(2L);
    f.setName("Facility 2");
    f.setCity("Lisboa");
    f.setPhoneNumber("987654321");
    f.setPostalCode("9213-842");
    f.setStreetName("Rua de Lisboa");
    f.setMaxRoomsCapacity(20);

    // create a facility first
    given()
      .contentType("application/json")
      .port(port)
      .body(JsonUtils.toJson(f))
    .when()
      .post("/facility/admin/add")
    .then()
      .statusCode(201);

    // delete the facility
    given()
      .contentType("application/json")
      .port(port)
    .when()
      .delete("/facility/admin/delete?id=2")
    .then()
      .statusCode(200);
    
  }

  @Test
  @DisplayName("Delete a facility that does not exist")
  void deleteFacilityThatDoesNotExist() {

    given()
      .port(port)
      .contentType("application/json")
    .when()
      .delete("/facility/admin/delete?id=2")
    .then()
      .statusCode(404);
    
  }

}