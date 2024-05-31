package deti.tqs.backend.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator.Validity;

import deti.tqs.backend.dtos.SpecialitySchema;
import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.services.SpecialityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    @PostMapping("/admin/create")
    public ResponseEntity<Speciality> createSpeciality(@Valid @RequestBody(required = true) SpecialitySchema specialitySchema) {
        try {
            Speciality special = new Speciality();
            special.setName(specialitySchema.name());
            special.setPrice(specialitySchema.price());
            special.setBeautyServiceId(specialitySchema.beautyServiceId());

            Speciality newSpecial = specialityService.save(special);
            
            return new ResponseEntity<>(newSpecial, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}
