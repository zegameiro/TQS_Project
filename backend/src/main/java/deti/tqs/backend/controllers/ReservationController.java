package deti.tqs.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.backend.dtos.ReservationSchema;
import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.models.Validity;
import deti.tqs.backend.services.ReservationService;
import deti.tqs.backend.services.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reservation")
@Tag(name = "Reservation", description = "Operations pertaining to reservations in the system.")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(FacilityController.class);

    private final ReservationService reservationService;
    private RoomService roomRepository;


    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/add")
    @Operation(summary = "Create a new reservation", description = "A customer make a reservation in the system.")
    public Reservation createReservation(@RequestBody(required = true) ReservationSchema reservationSchema) {
        logger.info("Creating reservation");
        Reservation reservation = new Reservation();
        reservation.setTimestamp(System.currentTimeMillis());
        reservation.setValidity(Validity.VALID);
        reservation.setSpeciality(reservationSchema.speciality());
        reservation.setRoom(roomRepository.findById(reservationSchema.roomID()));
        reservation.setCustomer(reservationSchema.customer());
        return reservationService.save(reservation);
    }
}
