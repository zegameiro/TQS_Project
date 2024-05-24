package deti.tqs.backend.integration.facility;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
class SearchFacilityTestsIT {

  private final static String BASE_URL = "http://localhost/api";

  private Facility f5 = new Facility();
  private Facility f6 = new Facility();

  @LocalServerPort
  private int port;

  /*
   * NECESSARY TESTS
   * 
   *  1. Search a facility with success
   *  2. Search a facility that does not exist
   *  3. Search for all facilities
   * 
  */

  @BeforeAll
  void setUp() {

    RestAssured.baseURI = BASE_URL;
    RestAssured.port = port;

    f5.setName("Facility 3");
    f5.setCity("Leiria");
    f5.setPhoneNumber("893721821");
    f5.setPostalCode("8915-029");
    f5.setStreetName("Rua da Batalha");
    f5.setMaxRoomsCapacity(10);

    f6.setName("Facility 4");
    f6.setCity("Porto");
    f6.setPhoneNumber("123456789");
    f6.setPostalCode("1234-567");
    f6.setStreetName("Rua do Porto");
    f6.setMaxRoomsCapacity(20);

  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("Search a facility with success")
  void searchFacilityWithSuccess() throws IOException {
    

    given()
      .contentType("application/json")
      .port(port)
      .body(JsonUtils.toJson(f5))
    .when()
      .post("/facility/admin/add")
    .then()
      .statusCode(201);

    HashMap<String, String> response =
      given()
        .contentType("application/json")
        .port(port)
      .when()
        .get("/facility/1")
      .then()
        .statusCode(200)
        .extract()
        .response()
      .as(HashMap.class);

    assertAll(
      () -> assertThat(response).containsEntry("name", "Facility 3"),
      () -> assertThat(response).containsEntry("city", "Leiria"),
      () -> assertThat(response).containsEntry("phoneNumber", "893721821"),
      () -> assertThat(response).containsEntry("postalCode", "8915-029"),
      () -> assertThat(response).containsEntry("streetName", "Rua da Batalha")
    );

  }

  @Test
  @DisplayName("Search a facility that does not exist")
  void searchFacilityThatDoesNotExist() {

    given()
      .port(port)
      .contentType("application/json")
    .when()
      .get("/facility/120")
    .then()
      .statusCode(404);

  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("Search for all facilities")
  void searchForAllFacilities() throws IOException {

    given()
      .contentType("application/json")
      .port(port)
      .body(JsonUtils.toJson(f6))
    .when()
      .post("/facility/admin/add")
    .then()
      .statusCode(201);

    List<Facility> response =
      given()
        .contentType("application/json")
        .port(port)
      .when()
        .get("/facility/all")
      .then()
        .statusCode(200)
        .extract()
        .response()
      .as(List.class);

    assertThat(response).hasSize(2);

  }

}