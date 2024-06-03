package deti.tqs.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.repositories.SpecialityRepository;
import jakarta.persistence.EntityExistsException;

@Service
public class SpecialityService {

    private SpecialityRepository specialityRepository;

    @Autowired
    SpecialityService(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    public Speciality save(Speciality speciality) throws Exception {

        // Check if there are some fields missing
        if(speciality.getName() == null)
            throw new NoSuchFieldException("Speciality must have all fields filled");

        Speciality found = specialityRepository.findByName(speciality.getName());
        
        // Check if a speciality with the same name already exists
        if (found != null)
            throw new EntityExistsException("Speciality with this name already exists");

        return specialityRepository.save(speciality);

    }

    public List<Speciality> getSpecialityByBeautyServiceId(int beautyServiceId) {
        return specialityRepository.findByBeautyServiceId(beautyServiceId);
    }
    
}
