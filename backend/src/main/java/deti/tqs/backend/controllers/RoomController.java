package deti.tqs.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.backend.dtos.RoomSchema;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.services.RoomService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/room")
public class RoomController {

  private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

  private final RoomService roomService;

  @Autowired
  RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @PostMapping("/admin/add")
  public ResponseEntity<Room> createRoom(@RequestBody(required = true) RoomSchema roomSchema)  {

    logger.info("Creating room");

    Room r = new Room();
    r.setName(roomSchema.name());
    r.setMaxChairsCapacity(roomSchema.maxChairsCapacity());

    Room savedRoom = null;

    try {

      savedRoom = roomService.save(r, roomSchema.facilityID());
      logger.info("Room created");

    } catch (NoSuchFieldException e) {

      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

    } catch (EntityNotFoundException e) {
      
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    } catch (EntityExistsException e) {

      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
      
    } catch (IllegalStateException e) {

      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);

    }

    return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);

  }

}