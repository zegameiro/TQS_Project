package deti.tqs.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import deti.tqs.backend.dtos.ReservationSchema;
import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/reservation")
@Tag(name = "Reservation", description = "Operations pertaining to reservations in the system.")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(FacilityController.class);

    private final ReservationService reservationService;


    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/add")
    @Operation(summary = "Create a new reservation", description = "A customer make a reservation in the system.")
    public ResponseEntity<Reservation> createReservation(@RequestBody(required = true) ReservationSchema reservationSchema) {

        logger.info("Creating reservation");

        Reservation res = null;

        try {

            res = reservationService.createReservation(reservationSchema);
            logger.info("Reservation created");

        } catch (EntityNotFoundException e) {

            logger.error("Error creating reservation: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (IllegalArgumentException e) {

            logger.error("Error creating reservation: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No employee available for the reservation");

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
        
    }

    @PostMapping("/checkin/{reservationID}")
    @Operation(summary = "Check-in a reservation", description = "A customer check-in a reservation in the system.")
    public ResponseEntity<Reservation> checkInReservation(@PathVariable(required = true) String reservationID) {

        logger.info("Checking in reservation");

        Reservation res = null;
        long id = Long.parseLong(reservationID);

        try {

            res = reservationService.updateReservationStatus(id, "1");
            logger.info("Reservation checked in");

        } catch (EntityNotFoundException e) {

            logger.error("Error checking in reservation: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (IllegalArgumentException e) {

            logger.error("Error checking in reservation: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());

        }

        return ResponseEntity.status(HttpStatus.OK).body(res);
        
    }

    @PostMapping("/pay/{reservationID}")
    @Operation(summary = "Pay a reservation", description = "A customer pay a reservation in the system.")
    public ResponseEntity<Reservation> payReservation(@PathVariable(required = true) String reservationID) {

        logger.info("Paying reservation");

        Reservation res = null;
        long id = Long.parseLong(reservationID);

        try {

            res = reservationService.updateReservationStatus(id, "4");
            logger.info("Reservation paid");

        } catch (EntityNotFoundException e) {

            logger.error("Error paying reservation: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (IllegalArgumentException e) {

            logger.error("Error paying reservation: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());

        }

        return ResponseEntity.status(HttpStatus.OK).body(res);
        
    }

    @GetMapping("/{reservationID}")
    @Operation(summary = "Get a reservation", description = "Get a reservation by its ID.")
    public ResponseEntity<Reservation> getReservation(@PathVariable(required = true) String reservationID) {

        logger.info("Getting reservation");

        Reservation res = null;
        long id = Long.parseLong(reservationID);

        try {

            res = reservationService.getReservation(id);
            logger.info("Reservation retrieved");

        } catch (EntityNotFoundException e) {

            logger.error("Error getting reservation: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        }

        return ResponseEntity.status(HttpStatus.OK).body(res);
        
    }

    @GetMapping("/customer/{email}")
    @Operation(summary = "Get reservations by customer email", description = "Get all reservations made by a customer.")
    public ResponseEntity<Iterable<Reservation>> reservationsByCustomerEmail(@PathVariable(required = true) String email) {

        logger.info("Getting reservations by customer");

        return ResponseEntity.status(HttpStatus.OK).body(reservationService.getReservationsByCustomerEmail(email));
        
    }

    @GetMapping("/employee/{employeeID}")
    @Operation(summary = "Get reservations by employee ID", description = "Get all reservations assigned to an employee.")
    public ResponseEntity<Iterable<Reservation>> getReservationsByEmployee(@PathVariable(required = true) String employeeID) {

        logger.info("Getting reservations by employee");

        Iterable<Reservation> res = null;
        long id = Long.parseLong(employeeID);

        try {

            res = reservationService.getReservationsByEmployeeID(id);
            logger.info("Reservations retrieved");

        } catch (EntityNotFoundException e) {

            logger.error("Error getting reservations by employee: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        }

        return ResponseEntity.status(HttpStatus.OK).body(res);
        
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get a reservation by secret code", description = "Get a reservation by its secret code.")
    public ResponseEntity<Reservation> getReservationByCode(@PathVariable(required = true) String code) {

        logger.info("Getting reservation by code");

        Reservation res = null;

        try io-8080-exec-9] .w.s.m.a.ResponseStatusExceptionResolver : Resolved [org.springframework.web.server.ResponseStatusException: 400 BAD_REQUEST "No employee available for the reservation"]
        {

            res = reservationService.getReservationBySecretCode(code);
            logger.info("Reservation retrieved");

        } catch (EntityNotFoundException e) {

            logger.error("Error getting reservation by code: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        }

        return ResponseEntity.status(HttpStatus.OK).body(res);
        
    }


}
