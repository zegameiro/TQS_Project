package deti.tqs.backend.services.facility;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
public class DeleteFacilityServiceTests {

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
   *  1. Delete a facility with a valid ID
   *  2. Delete a facility with an invalid ID
   * 
   */

  @BeforeEach
  void setUp() throws Exception {

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

    facilityService.save(fac1);
    facilityService.save(fac2);
    facilityService.save(fac3);

  }

  @Test
  @DisplayName("When deleting a facility with a valid ID, it should delete the facility")
  void whenDeleteFacilityWithValidID_ThenDeleteFacility() {

    when(facilityRepository.findById(fac1.getId())).thenReturn(fac1);

    facilityService.delete(fac1.getId());
    
    List<Facility> facilities = facilityService.getAllFacilities();

    assertThat(facilities).isNotNull().isEmpty();

    verify(facilityRepository, times(1)).findById(anyLong());
    verify(facilityRepository, times(1)).delete(any());

  }

  @Test
  @DisplayName("When deleting a facility with an invalid ID, it should throw an exception")
  void whenDeleteFacilityWithInvalidID_ThenThrowException() {

    when(facilityRepository.findById(100L)).thenReturn(null);

    assertThatThrownBy(() -> facilityService.delete(100L))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Facility not found");

    verify(facilityRepository, times(1)).findById(anyLong());
    verify(facilityRepository, never()).delete(any());

  }
  
}
