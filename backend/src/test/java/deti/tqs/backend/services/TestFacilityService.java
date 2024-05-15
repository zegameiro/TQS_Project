package deti.tqs.backend.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
  void whenSearchValidFacilityID_thenReturnValidFacility() {

    fac3.setId(3L);

    when(facilityService.getFacilityById(3L)).thenReturn(fac3);

    Facility foundFacility = facilityService.getFacilityById(3L);
    assertThat(foundFacility).isNotNull().isEqualTo(fac3);

    verify(facilityRepository, times(1)).findById(anyLong());

  }

  @Test
  void whenInvalidFacilityID_thenReturnNull() {

    when(facilityService.getFacilityById(100L)).thenReturn(null);

    Facility foundFacility = facilityService.getFacilityById(100L);
    assertThat(foundFacility).isNull();

    verify(facilityRepository, times(1)).findById(anyLong());

  }

  @Test
  void whenSearchAllFacilities_thenReturnAllFacilities() {

    when(facilityService.getAllFacilities()).thenReturn(List.of(fac1, fac2, fac3));

    List<Facility> facilities = facilityService.getAllFacilities();
    assertThat(facilities).isNotNull().hasSize(3).contains(fac1, fac2, fac3);

    verify(facilityRepository, times(1)).findAll();

  }

}
