package deti.tqs.backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import deti.tqs.backend.dtos.ChairSchema;
import deti.tqs.backend.models.Chair;
import deti.tqs.backend.services.ChairService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{id}")
    public ResponseEntity<Chair> getChair(@PathVariable(required = true) long id) {
        
        logger.info("Getting chair");

        Chair c = null;

        try {

            c = chairService.getChair(id);
            logger.info("Chair found");

        } catch (EntityNotFoundException e) {

            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }

        return ResponseEntity.status(200).body(c);

    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Chair>> getAllChairs(@RequestParam(required = false) String roomID) {
        
        logger.info("Getting all chairs");

        Iterable<Chair> chairs = null;

        if (roomID != null)
            chairs = chairService.getChairsByRoomID(Long.parseLong(roomID));
        else 
            chairs = chairService.getAllChairs();

        return ResponseEntity.status(200).body(chairs);

    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<Void> deleteChair(@RequestParam(required = true) long id) {
        
        logger.info("Deleting chair");

        try {

            chairService.deleteChair(id);
            logger.info("Chair deleted");

        } catch (EntityNotFoundException e) {

            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }

        return ResponseEntity.status(200).body(null);

    }

}
