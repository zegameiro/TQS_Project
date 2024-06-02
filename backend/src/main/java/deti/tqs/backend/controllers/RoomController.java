package deti.tqs.backend.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.backend.dtos.RoomSchema;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.services.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/room")
@Tag(name = "Room", description = "Operations pertaining to rooms in the system.")
public class RoomController {

  private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

  private final RoomService roomService;

  @Autowired
  RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @PostMapping("/admin/add")
  @Operation(summary = "Create a new room", description = "An admin create a new room in the system.")
  public ResponseEntity<Room> createRoom(@RequestBody(required = true) RoomSchema roomSchema)  {

    logger.info("Creating room");

    Room r = new Room();
    r.setName(roomSchema.name());
    r.setMaxChairsCapacity(roomSchema.maxChairsCapacity());
    r.setBeautyServiceId(Integer.parseInt(roomSchema.beautyServiceID()));

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

  @GetMapping("/{id}")
  @Operation(summary = "Get a room by ID", description = "Get a room by its ID.")
  public ResponseEntity<Room> getRoomById(@PathVariable long id) {

    logger.info("Getting room by ID");

    Room foundRoom = null;

    try {

      foundRoom = roomService.findById(id);
      logger.info("Room found");

    } catch (EntityNotFoundException e) {

      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    }

    return ResponseEntity.status(HttpStatus.OK).body(foundRoom);

  }

  @GetMapping("/all")
  @Operation(summary = "Get all rooms", description = "Get all rooms in the system.")
  public ResponseEntity<Iterable<Room>> getAllRooms() {

    logger.info("Getting all rooms");

    Iterable<Room> rooms = roomService.findAllRooms();

    return ResponseEntity.status(HttpStatus.OK).body(rooms);

  }

  @GetMapping("/search")
  @Operation(summary = "Search for a room", description = "Search for a room by its name and facility ID.")
  public ResponseEntity<List<Room>> searchRoom(@RequestParam(required = false) String roomName, @RequestParam(required = false) String facilityID) throws Exception {

    logger.info("Searching for room");

    long facilityIDLong = Long.parseLong(facilityID);
    List<Room> foundRoom = null;

    try {

      foundRoom = roomService.searchByFacilityInfo(roomName, facilityIDLong);
      logger.info("Found a room");

    } catch (EntityNotFoundException e) {

      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    } catch (NoSuchFieldException e) {

      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

    }

    return ResponseEntity.status(HttpStatus.OK).body(foundRoom);

  } 

  @PutMapping("/admin/update")
  @Operation(summary = "Update a room", description = "An admin update a room in the system.")
  public ResponseEntity<Room> updateRoom(@RequestBody(required = true) RoomSchema roomSchema, @RequestParam(required = true) long id) {

    logger.info("Updating room");

    Room room = new Room();
    room.setName(roomSchema.name());
    room.setMaxChairsCapacity(roomSchema.maxChairsCapacity());

    Room updatedRoom = null;

    try {
      
      updatedRoom = roomService.updateRoom(room, id, roomSchema.facilityID());
      
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

    return ResponseEntity.status(HttpStatus.OK).body(updatedRoom);

  }

  @DeleteMapping("/admin/delete")
  @Operation(summary = "Delete a room", description = "An admin delete a room in the system.")
  public ResponseEntity<Void> deleteRoom(@RequestParam(required = true) long id) {

    logger.info("Deleting room");

    try {

      roomService.deleteRoom(id);
      logger.info("Room deleted");

    } catch (EntityNotFoundException e) {

      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    }

    return ResponseEntity.status(HttpStatus.OK).body(null);

  }

}