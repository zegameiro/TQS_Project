package deti.tqs.backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import deti.tqs.backend.dtos.ChairSchema;
import deti.tqs.backend.models.Chair;
import deti.tqs.backend.services.ChairService;
import jakarta.persistence.EntityExistsException;
import java.lang.IllegalArgumentException;

import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/chair")
public class ChairController {

    private static final Logger log = LoggerFactory.getLogger(ChairController.class);
    private final ChairService chairService;

    public ChairController(ChairService chairService) {
        this.chairService = chairService;
    }

    @PostMapping("/admin/add")
    public ResponseEntity<Chair> createChair(@RequestBody(required = true) ChairSchema chairSchema) {
        log.info("Creating a new chair.");
        
        HttpStatus status = HttpStatus.CREATED;

        try {
            Chair chair = new Chair();
            chair.setName(chairSchema.name());
            chair.setRoom(chairSchema.room());
            Chair newChair = chairService.addChair(chair);
            return new ResponseEntity<Chair>(newChair, status);
        } catch (EntityExistsException e) {
            status = HttpStatus.CONFLICT;
        } catch (IllegalArgumentException e) { // Handle IllegalArgumentException
            status = HttpStatus.BAD_REQUEST;
        } 

        return new ResponseEntity<Chair>(status);
    }
}
