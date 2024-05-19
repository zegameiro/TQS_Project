package deti.tqs.backend.services.facility;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import jakarta.persistence.EntityExistsException;

@ExtendWith(MockitoExtension.class)
class AddFacilityTests {
  
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
   *  1. Save a valid facility
   *  2. Save a facility with a name that already exists
   *  3. Save a facility with missing fields
   * 
  */

  @BeforeEach
  void setUp() {

    fac1.setName("Facility 1");
    fac1.setCity("Aveiro");
    fac1.setPhoneNumber("123456789");
    fac1.setPostalCode("3810-193");
    fac1.setStreetName("Rua de Aveiro");

    fac2.setName("Facility 2");
    fac2.setCity("Porto");
    fac2.setPhoneNumber("987654321");
    fac2.setPostalCode("4000-007");
    fac2.setStreetName("Rua do Porto");

    fac3.setName("Facility 3");
    fac3.setCity("Lisboa");
    fac3.setPhoneNumber("823741291");
    fac3.setPostalCode("1938-092");
    fac3.setStreetName("Rua de Lisboa");

  }

  @Test
  @DisplayName("When saving a valid facility, it should return the facility saved")
  void whenSaveValidFacility_thenFacilityShouldBeReturn() throws Exception {
    
    when(facilityService.save(fac1)).thenReturn(fac1);

    Facility savedFacility = facilityService.save(fac1);
    assertThat(savedFacility).isNotNull().isEqualTo(savedFacility);

    when(facilityService.save(fac2)).thenReturn(fac2);

    savedFacility = facilityService.save(fac2);
    assertThat(savedFacility).isNotNull().isEqualTo(savedFacility);

    verify(facilityRepository, times(2)).save(any());

  }

  @Test
  @DisplayName("When saving a facility with a name that already exists, it should throw an exception")
  void whenSaveFacilityWithExistingName_thenThrowException() {

    Facility f = new Facility();
    f.setName("Facility 3");
    f.setCity("Lisboa");
    f.setPhoneNumber("823741291");
    f.setPostalCode("1938-092");
    f.setStreetName("Rua de Lisboa");

    when(facilityRepository.findByName("Facility 3")).thenReturn(fac3);
    
    assertThatThrownBy(() -> facilityService.save(f))
      .isInstanceOf(EntityExistsException.class)
      .hasMessage("Facility with this name already exists");
    
    verify(facilityRepository, never()).save(any());

  }

  @Test
  @DisplayName("When saving a facility with missing fields, it should throw an exception")
  void whenSaveFacilityWithMissingFields_thenThrowException() {

    Facility f = new Facility();
    f.setName("Facility 4");
    f.setCity("Aveiro");
    f.setPostalCode("3810-193");

    assertThatThrownBy(() -> facilityService.save(f))
      .isInstanceOf(NoSuchFieldException.class)
      .hasMessage("Facility must have all fields filled");

    verify(facilityRepository, never()).save(any());

  }

}
