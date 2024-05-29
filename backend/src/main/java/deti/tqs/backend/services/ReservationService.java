package deti.tqs.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.tqs.backend.dtos.ReservationSchema;
import deti.tqs.backend.models.Employee;
import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.models.ReservationQueue;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.repositories.ReservationRepository;
import deti.tqs.backend.repositories.RoomRepository;
import deti.tqs.backend.repositories.SpecialityRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;
    private SpecialityRepository specialityRepository;

    @Autowired
    ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, SpecialityRepository specialityRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.specialityRepository = specialityRepository;
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public void deleteReservation(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    public Reservation createReservation(ReservationSchema reservationSchema) {
        
        Reservation reservation = new Reservation();

        // Check if the room exists
        Room roomFound = roomRepository.findById(Long.parseLong(reservationSchema.roomID()));

        if(roomFound == null)
            throw new EntityNotFoundException("Room not found");

        // Check if the speciality exists
        Speciality specialityFound = specialityRepository.findById(Long.parseLong(reservationSchema.specialityID()));

        if(specialityFound == null)
            throw new EntityNotFoundException("Invalid speciality");

        // Check if there is an employee available for the reservation
        Employee designatedEmployee = findEmployeeForReservation(reservationSchema);

        if(designatedEmployee == null)
            throw new IllegalArgumentException("No employee available for the reservation");

        // Get the the reservation queue of the facility to add this reservation
        ReservationQueue reserQueue = reservation.getRoom().getFacility().getReservationQueue();

        if(reserQueue == null)
            throw new EntityNotFoundException("Reservation queue does not exist");

        // Set the reservation attributes
        reservation.setTimestamp(Long.parseLong(reservationSchema.timestamp()));
        reservation.setSecretCode(reservationSchema.secretCode());
        reservation.setCustomerName(reservationSchema.customerName());
        reservation.setCustomerEmail(reservationSchema.customerEmail());
        reservation.setCustomerPhoneNumber(reservationSchema.customerPhoneNumber());
        reservation.setSpeciality(specialityFound);
        reservation.setEmployee(designatedEmployee);
        reservation.setReservationQueue(reserQueue);

        if(designatedEmployee.getReservations() == null)
            designatedEmployee.setReservations(List.of(reservation));

        else
            designatedEmployee.getReservations().add(reservation);

        if(reserQueue.getReservations() == null)
            reserQueue.setReservations(List.of(reservation));

        else
            reserQueue.getReservations().add(reservation);


        return reservationRepository.save(reservation);

    }

    public Reservation getReservation(long reservationID) {

        Reservation found = reservationRepository.findById(reservationID);

        if(found == null)
            throw new EntityNotFoundException("Reservation not found");

        return found;
    }

    public List<Reservation> getReservationsByCustomerEmail(String email) {

        return reservationRepository.findByCustomerEmail(email);

    }

    public List<Reservation> getReservationsByEmployeeID(long employeeID) {

        return reservationRepository.findByEmployee_Id(employeeID);

    }

    public Reservation getReservationBySecretCode(String secretCode) {

        return reservationRepository.findBySecretCode(secretCode);

    }


    public Reservation updateReservationStatus(long reservationID, String status) {

        Reservation reserv = reservationRepository.findById(reservationID);

        if(reserv == null)
            throw new EntityNotFoundException("Reservation not found");

        switch(status) {

            case "0":
                reserv.setValidityID(0);
                break;

            case "1":
                reserv.setValidityID(1);
                break;

            case "2":
                reserv.setValidityID(2);
                break;

            case "3":
                reserv.setValidityID(3);
                break;

            case "4":
                reserv.setValidityID(4);
                break;

            default:
                throw new IllegalArgumentException("Invalid status");

        }

        return reservationRepository.save(reserv);

    }

    public Employee findEmployeeForReservation(ReservationSchema r) {
        
        Room roomFound = roomRepository.findById(Long.parseLong(r.roomID()));

        if(roomFound == null)
            throw new EntityNotFoundException("Room not found");


        List<Employee> employees = roomFound.getFacility().getEmployees();
        Speciality speciality = specialityRepository.findById(Long.parseLong(r.specialityID()));
        
        long timestamp = Long.parseLong(r.timestamp());
        boolean found = false;
        Employee designatedEmployee = null;

        for (Employee e : employees) {
            
            // Check if the employee has the required speciality for the reservation
            if(e.getSpecialities().contains(speciality)) {

                // Check if the employee has a reservation at the same time
                for(Reservation res : e.getReservations()) {

                    // If it has a reservation that is inside a time period of half hour from the reservation timestamp, break the loop and go to the next employee
                    if(res.getTimestamp() - 1800000 < timestamp && timestamp < res.getTimestamp() + 1800000) {

                        break;

                    // If it doesn't have a reservation at the same time, assign the employee to the reservation
                    } else {

                        designatedEmployee = e;
                        found = true;
                        break;

                    }
                }

            }

            if(found)
                break;

        }

        return designatedEmployee;

    }


    
}
