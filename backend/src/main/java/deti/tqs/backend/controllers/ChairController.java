package deti.tqs.backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import deti.tqs.backend.dtos.ChairSchema;
import deti.tqs.backend.models.Chair;
import deti.tqs.backend.services.ChairService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/chair")
public class ChairController {

    private static final Logger logger = LoggerFactory.getLogger(ChairController.class);

    private final ChairService chairService;

    ChairController(ChairService chairService) {
        this.chairService = chairService;
    }

    @PostMapping("/admin/add")
    public ResponseEntity<Chair> createChair(@RequestBody(required = true) ChairSchema chairSchema) throws Exception {
        
        logger.info("Creating chair");

        Chair c = new Chair();
        c.setName(chairSchema.name());

        Chair savedChair = null;

        try {

            savedChair = chairService.addChair(c, chairSchema.roomID());
            logger.info("Chair created");

        } catch (EntityNotFoundException e) {

            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        } catch (NoSuchFieldException e) {

            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        } catch (IllegalStateException e) {

            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);

        } catch (EntityExistsException e) {

            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

        }

        return ResponseEntity.status(201).body(savedChair);

    }


    
}
