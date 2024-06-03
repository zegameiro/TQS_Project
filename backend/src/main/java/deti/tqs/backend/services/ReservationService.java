package deti.tqs.backend.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.tqs.backend.dtos.ReservationSchema;
import deti.tqs.backend.models.Employee;
import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.models.ReservationQueue;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.repositories.EmployeeRepository;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.ReservationQueueRepository;
import deti.tqs.backend.repositories.ReservationRepository;
import deti.tqs.backend.repositories.RoomRepository;
import deti.tqs.backend.repositories.SpecialityRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private static final String RESERVATION_NOT_FOUND = "Reservation not found";

    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;
    private SpecialityRepository specialityRepository;
    private ReservationQueueRepository reservationQueueRepository;
    private FacilityRepository facilityRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, SpecialityRepository specialityRepository, 
        ReservationQueueRepository reservationQueueRepository, FacilityRepository facilityRepository, EmployeeRepository employeeRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.specialityRepository = specialityRepository;
        this.reservationQueueRepository = reservationQueueRepository;
        this.facilityRepository = facilityRepository;
        this.employeeRepository = employeeRepository;
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
            throw new EntityNotFoundException(RESERVATION_NOT_FOUND);

        // Check if the speciality exists
        Speciality specialityFound = specialityRepository.findById(Long.parseLong(reservationSchema.specialityID()));

        if(specialityFound == null)
            throw new EntityNotFoundException("Invalid speciality");

        // Check if there is an employee available for the reservation
        Employee designatedEmployee = findEmployeeForReservation(reservationSchema, roomFound, specialityFound);

        if(designatedEmployee == null)
            throw new IllegalArgumentException("No employee available for the reservation");

        // Get the the reservation queue of the facility to add this reservation
        ReservationQueue reserQueue = reservationQueueRepository.findById(roomFound.getFacility().getReservationQueueId());

        if(reserQueue == null) {
        
            reserQueue = new ReservationQueue();
            roomFound.getFacility().setReservationQueueId(reserQueue.getId());

            reservationQueueRepository.save(reserQueue);
            facilityRepository.save(roomFound.getFacility());

            logger.info("Reservation queue created");

        }

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

        employeeRepository.save(designatedEmployee);

        if(reserQueue.getReservations() == null)
            reserQueue.setReservations(List.of(reservation));

        else
            reserQueue.getReservations().add(reservation);


        return reservationRepository.save(reservation);

    }

    public Reservation getReservation(long reservationID) {

        Reservation found = reservationRepository.findById(reservationID);

        if(found == null)
            throw new EntityNotFoundException(RESERVATION_NOT_FOUND);

        return found;
    }

    public List<Reservation> getReservationsByCustomerEmail(String email) {

        return reservationRepository.findByCustomerEmail(email);

    }

    public List<Reservation> getReservationsByEmployeeID(long employeeID) {

        return reservationRepository.findByEmployeeId(employeeID);

    }

    public Reservation getReservationBySecretCode(String secretCode) {

        Reservation found = reservationRepository.findBySecretCode(secretCode);

        if(found == null)
            throw new EntityNotFoundException(RESERVATION_NOT_FOUND);

        return found;

    }


    public Reservation updateReservationStatus(long reservationID, String status) {

        Reservation reserv = reservationRepository.findById(reservationID);

        if(reserv == null)
            throw new EntityNotFoundException(RESERVATION_NOT_FOUND);

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

    public Employee findEmployeeForReservation(ReservationSchema r, Room roomFound, Speciality specialityFound) {

        if(roomFound == null || specialityFound == null || r == null)
            return null;

        List<Employee> employees = employeeRepository.findByFacility_Id(roomFound.getFacility().getId());

        if(employees == null || employees.isEmpty())
            return null;

        long timestamp = Long.parseLong(r.timestamp());
        Employee designatedEmployee = null;

        for (Employee e : employees) {

            // Check if the employee has the required speciality for the reservation
            if(e.getSpecialitiesID() != null && e.getSpecialitiesID().contains(specialityFound.getId())) {   

                logger.info("Employee has the speciality");

                // Check if the employee has a reservation at the same time
                
                if(e.getReservations() == null || e.getReservations().isEmpty()) {

                    designatedEmployee = e;

                    logger.info("Employee found with no reservations");

                    break;

                } else {

                    for(Reservation employeeReservation : e.getReservations()) {

                        // If it has a reservation that is inside a time period of half hour from the reservation timestamp, break the loop and go to the next employee
                        if(timestamp < employeeReservation.getTimestamp() + 1800000) {

                            logger.info("Employee has a reservation at the same time");
                            break;

                        // If it doesn't have a reservation at the same time, assign the employee to the reservation
                        } else {

                            designatedEmployee = e;
                            logger.info("Employee found");

                        }
                    }
                }

            }

        }

        return designatedEmployee;

    }


    
}
