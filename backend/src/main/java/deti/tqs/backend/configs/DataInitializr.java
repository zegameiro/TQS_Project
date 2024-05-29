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

import deti.tqs.backend.models.BeautyService;
import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Reservation;
import deti.tqs.backend.models.ReservationQueue;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.models.Speciality;
import deti.tqs.backend.models.Validity;
import deti.tqs.backend.repositories.ChairRepository;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.ReservationQueueRepository;
import deti.tqs.backend.repositories.ReservationRepository;
import deti.tqs.backend.repositories.RoomRepository;
import deti.tqs.backend.repositories.SpecialityRepository;

@Component
@Generated
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "dev")
public class DataInitializr implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializr.class);

    private final FacilityRepository facilityRepository;
    private final RoomRepository roomRepository;
    private final ChairRepository chairRepository;
    private final ReservationRepository reservationRepository;
    private final SpecialityRepository specialityRepository;
    private final ReservationQueueRepository reservationQueueRepository;

    @Autowired
    public DataInitializr(FacilityRepository facilityRepository, ReservationRepository reservationRepository,
            RoomRepository roomRepository, ChairRepository chairRepository, SpecialityRepository specialityRepository, ReservationQueueRepository reservationQueueRepository) {
        this.facilityRepository = facilityRepository;
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.chairRepository = chairRepository;
        this.specialityRepository = specialityRepository;
        this.reservationQueueRepository = reservationQueueRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Remove all data before add new data
        logger.info("Removing all data");

        reservationRepository.deleteAll();
        chairRepository.deleteAll();
        roomRepository.deleteAll();
        facilityRepository.deleteAll();
        specialityRepository.deleteAll();
        reservationQueueRepository.deleteAll();

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
        lisbon.setEmployees(new ArrayList<>());

        Facility porto = new Facility();
        porto.setName("Porto Beauty Plaza");
        porto.setCity("Porto");
        porto.setStreetName("Rua de Santa Catarina");
        porto.setPostalCode("4000-447");
        porto.setPhoneNumber("222 003 000");
        porto.setMaxRoomsCapacity(5);
        porto.setRooms(new ArrayList<>());
        porto.setEmployees(new ArrayList<>());

        Facility aveiro = new Facility();
        aveiro.setName("Aveiro Beauty Plaza");
        aveiro.setCity("Aveiro");
        aveiro.setStreetName("Avenida Dr. Lourenço Peixinho");
        aveiro.setPostalCode("3800-159");
        aveiro.setPhoneNumber("234 234 234");
        aveiro.setMaxRoomsCapacity(4);
        aveiro.setRooms(new ArrayList<>());
        aveiro.setEmployees(new ArrayList<>());

        List<Facility> facilities = List.of(lisbon, porto, aveiro);
        facilityRepository.saveAll(facilities);
        logger.info("Facility Lisbon, Porto and Aveiro created");


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

        // Speciality

        logger.info("Creating default specialities");

        Speciality speciality1 = new Speciality();
        speciality1.setName("Haircut");
        speciality1.setPrice(10.0);
        speciality1.setBeautyServiceId(0);

        Speciality speciality2 = new Speciality();
        speciality2.setName("Beard Trimming");
        speciality2.setPrice(38.01);
        speciality2.setBeautyServiceId(0);

        Speciality speciality3 = new Speciality();
        speciality3.setName("Extensions");
        speciality3.setPrice(58.18);
        speciality3.setBeautyServiceId(0);

        Speciality speciality4 = new Speciality();
        speciality4.setName("Coloring");
        speciality4.setPrice(1092.73);
        speciality4.setBeautyServiceId(0);

        Speciality speciality5 = new Speciality();
        speciality5.setName("Straightening");
        speciality5.setPrice(82.71);
        speciality5.setBeautyServiceId(0);

        Speciality speciality6 = new Speciality();
        speciality6.setName("Curling");
        speciality6.setPrice(293.54);
        speciality6.setBeautyServiceId(0);

        Speciality speciality7 = new Speciality();
        speciality7.setName("Wax Hair Removal");
        speciality7.setPrice(756.13);
        speciality7.setBeautyServiceId(1);

        Speciality speciality8 = new Speciality();
        speciality8.setName("Laser Hair Removal");
        speciality8.setPrice(10983.38);
        speciality8.setBeautyServiceId(1);

        Speciality speciality9 = new Speciality();
        speciality9.setName("Manicure");
        speciality9.setPrice(47.18);
        speciality9.setBeautyServiceId(2);

        Speciality speciality10 = new Speciality();
        speciality10.setName("Pedicure");
        speciality10.setPrice(15.0);
        speciality10.setBeautyServiceId(3);

        Speciality speciality11 = new Speciality();
        speciality11.setName("Massages");
        speciality11.setPrice(563.29);
        speciality11.setBeautyServiceId(4);

        Speciality speciality12 = new Speciality();
        speciality12.setName("Facial Treatments");
        speciality12.setPrice(672.12);
        speciality12.setBeautyServiceId(4);

        Speciality speciality13 = new Speciality();
        speciality13.setName("Dermatological treatments");
        speciality13.setPrice(2039.51);
        speciality13.setBeautyServiceId(4);

        Speciality speciality14 = new Speciality();
        speciality14.setName("Dermatological treatments");
        speciality14.setPrice(172.35);
        speciality14.setBeautyServiceId(4);

        Speciality speciality15 = new Speciality();
        speciality15.setName("Saunas");
        speciality15.setPrice(51821.74);
        speciality15.setBeautyServiceId(4);

        Speciality speciality16 = new Speciality();
        speciality16.setName("Jacuzzi");
        speciality16.setPrice(1000.05);
        speciality16.setBeautyServiceId(4);

        Speciality speciality17 = new Speciality();
        speciality17.setName("Turkish Bath");
        speciality17.setPrice(4720.20);
        speciality17.setBeautyServiceId(4);

        Speciality speciality18 = new Speciality();
        speciality18.setName("Pools");
        speciality18.setPrice(5.29);
        speciality18.setBeautyServiceId(4);


        List<Speciality> specialities = List.of(
            speciality1,
            speciality2,
            speciality3,
            speciality4,
            speciality5,
            speciality6,
            speciality7,
            speciality8,
            speciality9,
            speciality10,
            speciality11,
            speciality12,
            speciality13,
            speciality14,
            speciality15,
            speciality16,
            speciality17,
            speciality18
        );

        specialityRepository.saveAll(specialities);

        // Reservation Queues

        for (Facility facility : facilities) {
            ReservationQueue reservationQueue = new ReservationQueue();
            reservationQueue.setFacility(facility);
            reservationQueue.setReservations(new ArrayList<>());
            reservationQueueRepository.save(reservationQueue);
            facility.setReservationQueue(reservationQueue);
            facilityRepository.save(facility);
        }

        // Reservation
        logger.info("Creating default reservations");

        // // criar 5 reservas para rooms e facilities aleatorios
        // for (int i = 0; i < 5; i++) {
        //     Facility facility = facilities.get(i % 3);
        //     Room room = facility.getRooms().get(i % facility.getMaxRoomsCapacity());

        //     Reservation reservation = new Reservation();
        //     reservation.setTimestamp(System.currentTimeMillis());
        //     reservation.setSpeciality(room.getName());
        //     reservation.setRoom(room);

        //     reservationRepository.save(reservation);
        //     room.getReservations().add(reservation);
        //     roomRepository.save(room);
        // } 
    }
}