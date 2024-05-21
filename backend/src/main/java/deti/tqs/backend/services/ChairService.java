package deti.tqs.backend.services;

import org.springframework.stereotype.Service;

import deti.tqs.backend.models.Chair;
import deti.tqs.backend.repositories.ChairRepository;
import jakarta.persistence.EntityExistsException;

@Service
public class ChairService {
    
    private final ChairRepository chairRepository;
    
    public ChairService(ChairRepository chairRepository) {
        this.chairRepository = chairRepository;
    }

    public Chair addChair(Chair chair) throws EntityExistsException, IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        if (chairRepository.findById(chair.getId()) != null) {
            throw new EntityExistsException("Chair already exists");
        }
        return chairRepository.save(chair);
    }

}
