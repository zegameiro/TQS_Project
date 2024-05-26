package deti.tqs.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.repositories.ReservationRepository;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    @Autowired
    ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
    
}
