package deti.tqs.backend.services.speciality;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.repositories.SpecialityRepository;
import deti.tqs.backend.services.SpecialityService;
import jakarta.persistence.EntityExistsException;

@ExtendWith(MockitoExtension.class)
class CreateSpecialityServiceTests {
  
  @Mock
  private SpecialityRepository specialityRepository;

  @InjectMocks
  private SpecialityService specialityService;

  /*
   * NECESSARY TESTS
   * 
   *  1. Create a speciality with success
   *  2. Attempt to create a speciality with the same name
   *  3. Attempt to create a speciality with missing fields
   * 
  */

  private Speciality speciality = new Speciality();
  private Speciality invalidSpecility = new Speciality();

  @BeforeEach
  void setUp() {

    speciality.setName("Speciality 1");
    speciality.setPrice(10.0);
    speciality.setBeautyServiceId(2);

    invalidSpecility.setName(null);
    invalidSpecility.setPrice(10.0);
    invalidSpecility.setBeautyServiceId(2);

  }

  @Test
  @DisplayName("Create a speciality with success")
  void createSpecialityWithSuccess() throws Exception {
    
    when(specialityRepository.findByName(anyString())).thenReturn(null);
    when(specialityRepository.save(speciality)).thenReturn(speciality);

    Speciality spe = specialityService.save(speciality);

    assertThat(spe).isNotNull().isEqualTo(speciality);

    verify(specialityRepository, times(1)).findByName(anyString());
    verify(specialityRepository, times(1)).save(any());

  }

  @Test
  @DisplayName("Attempt to create a speciality with the same name")
  void createSpecialityWithSameName() {
    
    when(specialityRepository.findByName(anyString())).thenReturn(speciality);

    assertThrows(EntityExistsException.class, () -> specialityService.save(speciality));

    verify(specialityRepository, times(1)).findByName(anyString());
    verify(specialityRepository, never()).save(any());

  }

  @Test
  @DisplayName("Attempt to create a speciality with missing fields")
  void createSpecialityWithMissingFields() {

    assertThrows(NoSuchFieldException.class, () -> specialityService.save(invalidSpecility));

    verify(specialityRepository, never()).findByName(anyString());
    verify(specialityRepository, never()).save(any());

  }




}
