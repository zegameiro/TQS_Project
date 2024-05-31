package deti.tqs.backend.services.speciality;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.repositories.SpecialityRepository;
import deti.tqs.backend.services.SpecialityService;

@ExtendWith(MockitoExtension.class)
class SearchSpecialityServiceTests {
  
  @Mock
  private SpecialityRepository specialityRepository;

  @InjectMocks
  private SpecialityService specialityService;

  /*
   * NECESSARY TESTS
   * 
   *  1. Search a speciality by the beauty service id
   * 
  */

  private Speciality haircut = new Speciality();
  private Speciality beardtrimming = new Speciality();

  @BeforeEach
  void setUp() {

    haircut.setName("Haircut");
    haircut.setPrice(29.21);
    haircut.setBeautyServiceId(1);

    beardtrimming.setName("Beard Trimming");
    beardtrimming.setPrice(15.0);
    beardtrimming.setBeautyServiceId(1);

  }

  @Test
  void searchSpecialityByBeautyServiceId() {

    when(specialityRepository.findByBeautyServiceId(1)).thenReturn(List.of(haircut, beardtrimming));

    List<Speciality> specialities = specialityService.getSpecialityByBeautyServiceId(1);

    assertThat(specialities).isNotNull().hasSize(2).contains(haircut, beardtrimming);

  }


}
