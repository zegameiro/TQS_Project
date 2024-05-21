package deti.tqs.backend.integration.facility;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.util.HashMap;

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
import deti.tqs.backend.dtos.FacilitySchema;
import deti.tqs.backend.models.Facility;
import io.restassured.RestAssured;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@TestInstance(Lifecycle.PER_CLASS)
class CreateFacilityTestsIT {
  
  private final static String BASE_URL = "http://localhost/api";

  @LocalServerPort
  private int port;

  /*
   * NECESSARY TESTS
   * 
   *  1. Create a facility with success
   *  2. create a facility with missing fields
   *  3. Create a facility with a name that already exists
   *  4. Create a facility with an invalid capacity
   * 
   */

  @BeforeAll
  void setUp() {

    RestAssured.baseURI = BASE_URL;
    RestAssured.port = port;

  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("Create a facility with success")
  void createFacilityWithSuccess() throws IOException {
    
    FacilitySchema f1 = new FacilitySchema(
      "Facility 1", 
      "Aveiro", 
      "123456789", 
      "3810-193", 
      "Rua de Aveiro", 
      10
    );
    
    HashMap<String, String> response = 
      given()
        .contentType("application/json")
        .port(port)
      .when()
        .body(JsonUtils.toJson(f1))
        .post("/facility/admin/add")
      .then()
        .statusCode(201)
        .extract()
        .response()
        .as(HashMap.class);

    assertAll(
      () -> assertThat(response).containsEntry("name", f1.name()),
      () -> assertThat(response).containsEntry("city", f1.city()),
      () -> assertThat(response).containsEntry("phoneNumber", f1.phoneNumber()),
      () -> assertThat(response).containsEntry("postalCode", f1.postalCode()),
      () -> assertThat(response).containsEntry("streetName", f1.streetName())
    );

  }

  @Test
  @DisplayName("Create a facility with missing fields")
  void createFacilityWithMissingFields() throws IOException {

    Facility f2 = new Facility();
    f2.setName("Facility 1");
    f2.setCity("Aveiro");
    f2.setPhoneNumber("123456789");
    f2.setPostalCode("3810-193");
     
    given()
      .contentType("application/json")
      .port(port)
    .when()
      .body(JsonUtils.toJson(f2))
      .post("/facility/admin/add")
    .then()
      .statusCode(400);

  }

  @Test
  @DisplayName("Create a facility with a name that already exists")
  void createFacilityWithExistingName() throws IOException {

    Facility f3 = new Facility();
    f3.setName("Facility 1");
    f3.setCity("Lisboa");
    f3.setPhoneNumber("987654321");
    f3.setPostalCode("3921-829");
    f3.setStreetName("Rua de Lisbon");
    f3.setMaxRoomsCapacity(30);
    
    given()
      .contentType("application/json")
      .port(port)
    .when()
      .body(JsonUtils.toJson(f3))
      .post("/facility/admin/add")
    .then()
      .statusCode(409);

  }

  @Test
  @DisplayName("Create a facility with an invalid capacity")
  void createFacilityWithInvalidCapacity() throws IOException {

    Facility f4 = new Facility();
    f4.setName("Facility 12");
    f4.setCity("Lisboa");
    f4.setPhoneNumber("987654321");
    f4.setPostalCode("3921-829");
    f4.setStreetName("Rua de Lisbon");
    f4.setMaxRoomsCapacity(-1);
    
    given()
      .contentType("application/json")
      .port(port)
    .when()
      .body(JsonUtils.toJson(f4))
      .post("/facility/admin/add")
    .then()
      .statusCode(400);

  }

}
