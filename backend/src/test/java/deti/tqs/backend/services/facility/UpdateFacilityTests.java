package deti.tqs.backend.services.facility;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
public class UpdateFacilityTests {

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
   *  1. Update a facility with success
   *  2. Update a facility that does not exist
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
  @DisplayName("When updating a facility with a valid ID, it should return the updated facility")
  void whenUpdateFacilityWithValidID_ThenReturnUpdatedFacility() {

    Facility f = new Facility();
    f.setId(fac1.getId());
    f.setName("Facility Number 1");
    f.setCity("Aveiro");
    f.setPhoneNumber("987245124");
    f.setPostalCode("3810-193");
    f.setStreetName("Rua de Aveiro Nº 14532");

    when(facilityRepository.findById(fac1.getId())).thenReturn(fac1);
    when(facilityRepository.save(fac1)).thenReturn(f);

    Facility updatedFacility = facilityService.update(f, fac1.getId());

    assertAll(
      () -> assertThat(updatedFacility).isNotNull().isEqualTo(f),
      () -> assertThat(updatedFacility.getName()).isEqualTo(f.getName()),
      () -> assertThat(updatedFacility.getStreetName()).isEqualTo(f.getStreetName()),
      () -> assertThat(updatedFacility.getPhoneNumber()).isEqualTo(f.getPhoneNumber())
    );

    verify(facilityRepository, times(1)).findById(anyLong());
    verify(facilityRepository, times(1)).save(any());

  }

  @Test
  @DisplayName("When updating a facility with an invalid ID, it should throw an exception")
  void whenUpdateFacilityWithInvalidID_ThenThrowException() {

    Facility f = new Facility();
    f.setId(100L);
    f.setName("Facility Number 1");
    f.setCity("Aveiro");
    f.setPhoneNumber("987245124");
    f.setPostalCode("3810-193");
    f.setStreetName("Rua de Aveiro Nº 14532");

    when(facilityRepository.findById(100L)).thenReturn(null);

    assertThatThrownBy(() -> facilityService.update(f, 100L))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Facility not found");

    verify(facilityRepository, times(1)).findById(anyLong());
    verify(facilityRepository, never()).save(any());

  }

}
