package deti.tqs.backend.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.backend.models.Chair;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

@DataJpaTest
class TestChairRepository {

  private ChairRepository chairRepository;

  @Autowired
  public TestChairRepository(ChairRepository chairRepository) {
    this.chairRepository = chairRepository;
  }

  @Test
  @DisplayName("Test save a chair with success")
  void testSaveChairWithSuccess() {

    Chair chair = new Chair();

    chair.setName("Chair 1");
    chair.setAvailable(false);
    chairRepository.save(chair);

    Chair c = chairRepository.findById(chair.getId());

    assertAll(
      () -> assertThat(c).isNotNull(),
      () -> assertThat(c.getName()).isEqualTo(chair.getName())
    );

  }

  @Test
  @DisplayName("Test find an non existing chair")
  void testFindNonExistingChair() {

    Chair c = chairRepository.findById(100L);

    assertThat(c).isNull();
  }

  @Test
  @DisplayName("Test find all saved existing chairs")
  void testFindAllExistingChairs() {

    Chair chair1 = new Chair();
    chair1.setName("Chair 1");
    chair1.setAvailable(false);

    Chair chair2 = new Chair();
    chair2.setName("Chair 2");
    chair2.setAvailable(true);

    Chair chair3 = new Chair();
    chair3.setName("Chair 3");
    chair3.setAvailable(false);

    chairRepository.saveAll(List.of(chair1, chair2, chair3));

    assertThat(chairRepository.findAll()).isNotNull().hasSize(3).contains(chair1, chair2, chair3);
  }
  
}
