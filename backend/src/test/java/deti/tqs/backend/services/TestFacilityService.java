package deti.tqs.backend.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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

@ExtendWith(MockitoExtension.class)
class TestFacilityService {
  
  @Mock
  private FacilityRepository facilityRepository;

  @InjectMocks
  private FacilityService facilityService;

  private Facility fac1 = new Facility();
  private Facility fac2 = new Facility();
  private Facility fac3 = new Facility();

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
  void whenSaveValidFacility_thenFacilityShouldBeReturn() {
    
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
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Facility with this name already exists");
    
    verify(facilityRepository, never()).save(any());

  }

  @Test
  @DisplayName("WHen searching for a valid facility ID, it should return the facility with that ID")
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

  @Test
  @DisplayName("When deleting a facility with a valid ID, it should delete the facility")
  void whenDeleteFacilityWithValidID_ThenDeleteFacility() {

    when(facilityRepository.findById(fac1.getId())).thenReturn(fac1);

    facilityService.delete(fac1.getId());
    
    List<Facility> facilities = facilityService.getAllFacilities();

    assertThat(facilities).isNotNull().hasSize(0);

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
