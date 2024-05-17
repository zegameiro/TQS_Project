package deti.tqs.backend.services;

import org.springframework.stereotype.Service;

import deti.tqs.backend.models.Chair;
import deti.tqs.backend.repositories.ChairRepository;

@Service
public class ChairService {
    
    private final ChairRepository chairRepository;
    
    public ChairService(ChairRepository chairRepository) {
        this.chairRepository = chairRepository;
    }

    public Chair addChair(Chair chair) {
        return chairRepository.save(chair);
    }

}
