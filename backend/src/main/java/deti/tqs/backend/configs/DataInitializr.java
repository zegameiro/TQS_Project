package deti.tqs.backend.configs;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Customer;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.models.Validity;
import deti.tqs.backend.repositories.ChairRepository;
import deti.tqs.backend.repositories.CustomerRepository;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.ReservationRepository;
import deti.tqs.backend.repositories.RoomRepository;

@Component
@Generated
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "dev")
public class DataInitializr implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializr.class);

    private final FacilityRepository facilityRepository;
    private final RoomRepository roomRepository;
    private final ChairRepository chairRepository;
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public DataInitializr(FacilityRepository facilityRepository, ReservationRepository reservationRepository,
            RoomRepository roomRepository, ChairRepository chairRepository, CustomerRepository customerRepository) {
        this.facilityRepository = facilityRepository;
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.chairRepository = chairRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Remove all data before add new data
        logger.info("Removing all data");
        reservationRepository.deleteAll();
        chairRepository.deleteAll();
        roomRepository.deleteAll();
        facilityRepository.deleteAll();

        logger.info("Creating default values");

        // Facility
        Facility lisbon = new Facility();
        lisbon.setName("Lisbon Beauty Plaza");
        lisbon.setCity("Lisbon");
        lisbon.setStreetName("Avenida da Liberdade");
        lisbon.setPostalCode("1250-096");
        lisbon.setPhoneNumber("213 244 000");
        lisbon.setMaxRoomsCapacity(6);
        lisbon.setRooms(new ArrayList<>());

        Facility porto = new Facility();
        porto.setName("Porto Beauty Plaza");
        porto.setCity("Porto");
        porto.setStreetName("Rua de Santa Catarina");
        porto.setPostalCode("4000-447");
        porto.setPhoneNumber("222 003 000");
        porto.setMaxRoomsCapacity(5);
        porto.setRooms(new ArrayList<>());

        Facility aveiro = new Facility();
        aveiro.setName("Aveiro Beauty Plaza");
        aveiro.setCity("Aveiro");
        aveiro.setStreetName("Avenida Dr. Lourenço Peixinho");
        aveiro.setPostalCode("3800-159");
        aveiro.setPhoneNumber("234 234 234");
        aveiro.setMaxRoomsCapacity(4);
        aveiro.setRooms(new ArrayList<>());

        List<Facility> facilities = List.of(lisbon, porto, aveiro);
        facilityRepository.save(lisbon);
        facilityRepository.save(porto);
        facilityRepository.save(aveiro);
        logger.info("Facility Lisbon created");
        logger.info("Facility Porto created");
        logger.info("Facility Aveiro created");


        // Room
        List<String> roomExamples = List.of("Basic Hairdresser", "Complex Hairdresser", "Makeup", "Depilation", "Manicure/Pedicure", "Spa");

        for (Facility facility : facilities) {
            for (int i = 0; i < facility.getMaxRoomsCapacity(); i++) {
                Room room = new Room();
                room.setName(roomExamples.get(i));
                room.setMaxChairsCapacity(5);
                room.setFacility(facility);
                room.setChairs(new ArrayList<>());
                room.setReservations(new ArrayList<>());
                roomRepository.save(room);
                facility.getRooms().add(room);
            }
            logger.info("Rooms for" + facility.getName() + " created");
            facilityRepository.save(facility);
        }

        // Chair
        for (Facility facility : facilities) {
            for (Room room : facility.getRooms()) {
                for (int i = 0; i < room.getMaxChairsCapacity(); i++) {
                    Chair chair = new Chair();
                    chair.setName("Chair " + i);
                    chair.setAvailable(true);
                    chair.setRoom(room);
                    chairRepository.save(chair);
                    room.getChairs().add(chair);
                }
                logger.info("Chairs for" + room.getName() + " created");
                roomRepository.save(room);
            }
        }

        // Costumer
        logger.info("Creating default costumer");
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setPhoneNumber("912345678");
        customer.setEmail("aaaaaaa@a.a");
        customer.setReservations(new ArrayList<>());
        customerRepository.save(customer);

        // Reservation
        logger.info("Creating default reservations");
        // criar 5 reservas para rooms e facilities aleatorios
        for (int i = 0; i < 5; i++) {
            Facility facility = facilities.get(i % 3);
            Room room = facility.getRooms().get(i % facility.getMaxRoomsCapacity());

            Reservation reservation = new Reservation();
            reservation.setTimestamp(System.currentTimeMillis());
            reservation.setValidity(Validity.VALID);
            reservation.setSpeciality(room.getName());
            reservation.setRoom(room);
            reservation.setCustomer(customer);
            reservationRepository.save(reservation);
            room.getReservations().add(reservation);
            roomRepository.save(room);
        } 
    }
}