package deti.tqs.backend.integration.facility;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

import deti.tqs.backend.models.Facility;
import io.restassured.RestAssured;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@TestInstance(Lifecycle.PER_CLASS)
public class UpdateFacilityTestsIT {

  private final static String BASE_URL = "http://localhost/api";

  @LocalServerPort
  private int port;

  /*
   * NECESSARY TESTS
   * 
   *  1. Update a facility with success
   *  2. Update a facility that does not exist
   * 
  */

  @BeforeAll
  void setUp() {

    RestAssured.baseURI = BASE_URL;
    RestAssured.port = port;

  }

  @Test
  @DisplayName("Update a facility with success")
  void updateFacilityWithSuccess() {
    
    Facility f6 = new Facility();
    f6.setName("Facility 5");
    f6.setCity("Santarém");
    f6.setPhoneNumber("837103912");
    f6.setPostalCode("7245-201");
    f6.setStreetName("Rua de Santarém");
    f6.setMaxRoomsCapacity(39);

    // Create the facility
    Facility createdFacility = 
      given()
        .contentType("application/json")
        .port(port)
        .body(f6)
      .when()
        .post("/facility/admin/add")
      .then()
        .statusCode(201)
        .extract()
        .response()
        .as(Facility.class);

    assertAll(
      () -> assertThat(createdFacility).isNotNull(),
      () -> assertThat(createdFacility.getName()).isEqualTo("Facility 5")
    );

    // Update the facility
    f6.setCity("Lagos");
    f6.setPostalCode("1263-291");
    f6.setStreetName("Rua de Lagos");
    f6.setMaxRoomsCapacity(12);

    Facility updatedFacility = 
      given()
        .contentType("application/json")
        .port(port)
        .param("id", createdFacility.getId())
        .body(f6)
      .when()
        .put("/facility/admin/update")
      .then()
        .statusCode(200)
        .extract()
        .response()
        .as(Facility.class);

    assertAll(
      () -> assertThat(updatedFacility).isNotNull(),
      () -> assertThat(updatedFacility.getName()).isEqualTo("Facility 5"),
      () -> assertThat(updatedFacility.getCity()).isEqualTo("Lagos"),
      () -> assertThat(updatedFacility.getPostalCode()).isEqualTo("1263-291"),
      () -> assertThat(updatedFacility.getStreetName()).isEqualTo("Rua de Lagos")
    );

  }

  @Test
  @DisplayName("Update a facility that does not exist")
  void updateFacilityThatDoesNotExist() {
    
    Facility f7 = new Facility();
    f7.setName("Facility 6");
    f7.setCity("Santarém");
    f7.setPhoneNumber("837103912");
    f7.setPostalCode("7245-201");
    f7.setStreetName("Rua de Santarém");
    f7.setMaxRoomsCapacity(12);

    // Update the facility
    given()
      .contentType("application/json")
      .port(port)
      .param("id", 12481L)
      .body(f7)
    .when()
      .put("/facility/admin/update")
    .then()
      .statusCode(404);

  }


  
}
