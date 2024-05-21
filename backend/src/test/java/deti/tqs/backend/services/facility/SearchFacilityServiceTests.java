package deti.tqs.backend.services.facility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.backend.models.Facility;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.services.FacilityService;

@ExtendWith(MockitoExtension.class)
class SearchFacilityServiceTests {
  
  @Mock
  private FacilityRepository facilityRepository;

  @InjectMocks
  private FacilityService facilityService;

  private Facility fac1 = new Facility();
  private Facility fac2 = new Facility();
  private Facility fac3 = new Facility();

  /*
   * NECESSARY TESTS
   * 
   *  1. Retrieve a facility with success by its id
   *  2. Retrieve a facility that does not exist
   *  3. Retrieve all facilities
   * 
   */

  @BeforeEach
  void setUp() {

    fac1.setName("Facility 1");
    fac1.setCity("Aveiro");
    fac1.setPhoneNumber("123456789");
    fac1.setPostalCode("3810-193");
    fac1.setStreetName("Rua de Aveiro");
    fac1.setMaxRoomsCapacity(10);

    fac2.setName("Facility 2");
    fac2.setCity("Porto");
    fac2.setPhoneNumber("987654321");
    fac2.setPostalCode("4000-007");
    fac2.setStreetName("Rua do Porto");
    fac2.setMaxRoomsCapacity(20);

    fac3.setName("Facility 3");
    fac3.setCity("Lisboa");
    fac3.setPhoneNumber("823741291");
    fac3.setPostalCode("1938-092");
    fac3.setStreetName("Rua de Lisboa");
    fac3.setMaxRoomsCapacity(30);

  }

  @Test
  @DisplayName("When searching for a valid facility ID, it should return the facility with that ID")
  void whenSearchValidFacilityID_thenReturnValidFacility() {

    fac3.setId(3L);

    when(facilityService.getFacilityById(3L)).thenReturn(fac3);

    Facility foundFacility = facilityService.getFacilityById(3L);
    assertThat(foundFacility).isNotNull().isEqualTo(fac3);

    verify(facilityRepository, times(1)).findById(anyLong());

  }

  @Test
  @DisplayName("When searching for an invalid facility ID, it should return null")
  void whenInvalidFacilityID_thenReturnNull() {

    when(facilityService.getFacilityById(100L)).thenReturn(null);

    Facility foundFacility = facilityService.getFacilityById(100L);
    assertThat(foundFacility).isNull();

    verify(facilityRepository, times(1)).findById(anyLong());

  }

  @Test
  @DisplayName("When searching for all facilities, it should return all facilities")
  void whenSearchAllFacilities_thenReturnAllFacilities() {

    when(facilityService.getAllFacilities()).thenReturn(List.of(fac1, fac2, fac3));

    List<Facility> facilities = facilityService.getAllFacilities();
    assertThat(facilities).isNotNull().hasSize(3).contains(fac1, fac2, fac3);

    verify(facilityRepository, times(1)).findAll();

  }

  @Test
  @DisplayName("When searching for a facility by name, it should return the facility with that name")
  void whenSearchFacilityName_ThenReturnCorrectFacility() {

    when(facilityService.getFacilityByName("Facility 2")).thenReturn(fac2);

    Facility foundFacility = facilityService.getFacilityByName("Facility 2");

    assertThat(foundFacility).isNotNull().isEqualTo(fac2);

    verify(facilityRepository, times(1)).findByName("Facility 2");

  }

  @Test
  @DisplayName("When searching for a facility by name that does not exist, it should return null")
  void whenSearchNonExistingFacilityName_ThenReturnNull() {

    when(facilityService.getFacilityByName("Facility ABCDE")).thenReturn(null);

    Facility foundFacility = facilityService.getFacilityByName("Facility ABCDE");

    assertThat(foundFacility).isNull();

    verify(facilityRepository, times(1)).findByName(anyString());

  }

}
