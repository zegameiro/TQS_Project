package deti.tqs.backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import deti.tqs.backend.models.Chair;
import deti.tqs.backend.services.ChairService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.IllegalArgumentException;

import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api")
public class ChairController {

    private final ChairService chairService;

    public ChairController(ChairService chairService) {
        this.chairService = chairService;
    }

    @PostMapping("/chair")
    public ResponseEntity<Chair> createChair(@RequestBody Chair chair) {
        HttpStatus status = HttpStatus.CREATED;

        try {
            Chair newChair = chairService.addChair(chair);
            return new ResponseEntity<Chair>(newChair, status);
        } catch (EntityExistsException e) {
            status = HttpStatus.CONFLICT;
        } catch (EntityNotFoundException e) {
            status = HttpStatus.NOT_FOUND;
        } catch (IllegalAccessException e) { // Handle validation errors
            status = HttpStatus.FORBIDDEN;
        } catch (NoSuchFieldException e) { // Handle NoSuchFieldException
            status = HttpStatus.BAD_REQUEST;
        } catch (IllegalArgumentException e) { // Handle IllegalArgumentException
            status = HttpStatus.BAD_REQUEST;
        } 

        return new ResponseEntity<Chair>(status);
    }
}
