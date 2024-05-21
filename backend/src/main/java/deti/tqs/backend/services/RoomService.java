package deti.tqs.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.tqs.backend.models.Facility;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.FacilityRepository;
import deti.tqs.backend.repositories.RoomRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RoomService {
  
  private RoomRepository roomRepository;
  private FacilityRepository facilityRepository;

  @Autowired
  RoomService(RoomRepository roomRepository, FacilityRepository facilityRepository) {
    this.roomRepository = roomRepository;
    this.facilityRepository = facilityRepository;
  }

  public Room save(Room room, long facilityID) throws NoSuchFieldException {

    // Check if the name is missing
    
    if(room.getName() == null || room.getName().length() == 0) 
      throw new NoSuchFieldException("Room must have a name");

    // Check if the capacity has a valid value

    if(room.getMaxChairsCapacity() <= 0)
      throw new NoSuchFieldException("Room must have a valid capacity value greater than 0"); 
    
    // Check if the facility exists

    Facility found = facilityRepository.findById(facilityID);

    if (found == null)
      throw new EntityNotFoundException("Facility does not exist");

    // Check if a room with the same name already exists in this facility

    Room roomSameName = roomRepository.findByNameAndFacilityId(room.getName(), facilityID);

    if (roomSameName != null)
      throw new EntityExistsException("Room with this name already exists in this facility");


    // Check if the facility is at full capacity

    int size = found.getRooms() == null ? 0 : found.getRooms().size();

    if(found.getMaxRoomsCapacity() <= size)
      throw new IllegalStateException("Facility is at full capacity");

    room.setFacility(found);

    return roomRepository.save(room);

  }

}
