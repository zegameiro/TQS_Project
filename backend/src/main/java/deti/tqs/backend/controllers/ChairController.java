package deti.tqs.backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import deti.tqs.backend.models.Chair;
import deti.tqs.backend.services.ChairService;

import org.springframework.web.bind.annotation.RequestMapping;
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
        // TODO: Implement this method;
        return null;
    }


    
}
