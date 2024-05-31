package deti.tqs.backend.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.services.SpecialityService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/speciality")
@Tag(name = "Speciality", description = "Operations pertaining to specialities in the system.")
public class SpecialityController {

    private static final Logger logger = LoggerFactory.getLogger(SpecialityController.class);

    private final SpecialityService specialityService;

    @Autowired
    SpecialityController(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Speciality>> getSpecialitiesByBeautyServiceId(@PathVariable int id) {
        logger.info("Getting specialities by beauty service id");

        List<Speciality> specialities = specialityService.getSpecialityByBeautyServiceId(id);

        logger.info("Specialities retrieved");

        return ResponseEntity.ok(specialities);
    }
    
}
