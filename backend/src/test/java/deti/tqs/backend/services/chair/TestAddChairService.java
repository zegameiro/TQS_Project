package deti.tqs.backend.services.chair;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.ChairRepository;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.RoomRepository;
import deti.tqs.backend.services.ChairService;
import groovy.lang.MissingFieldException;
import jakarta.persistence.EntityExistsException;


@ExtendWith(MockitoExtension.class)
public class TestAddChairService {

    /* NECESSARY TESTS */
    /*
    * 1. Save a valid chair with success in existing room
    * 2. Fail to save an invalid chair in non existing room 
    */
    
    @Mock
    private ChairRepository chairRepository;

    @InjectMocks
    private ChairService chairService;

    private Room room = new Room();


    @Test
    void addChair_whenChairAlreadyExists_shouldThrowEntityExistsException() {

        Chair chair = new Chair();
        chair.setName("Good Chair");
        chair.setRoom(room);

        when(chairRepository.findById(anyLong())).thenReturn(chair);

        // Act & Assert
        assertThrows(EntityExistsException.class, () -> chairService.addChair(chair));
    }


    @Test
    @DisplayName("Test save a valid chair with success")
    public void testSaveChairInValidRoom() {
        
        Chair chair = new Chair();
        chair.setName("Good Chair");
        chair.setRoom(room);

        Mockito.when(chairRepository.save(any())).thenReturn(chair);

        Chair savedChair = chairService.addChair(chair);

        assertAll(
            () -> assertThat(savedChair).isNotNull(),
            () -> assertThat(savedChair.getName()).isEqualTo(chair.getName()),
            () -> assertThat(savedChair.getRoom()).isEqualTo(chair.getRoom())
        );

        verify(chairRepository, times(1)).save(any());
    }

}
