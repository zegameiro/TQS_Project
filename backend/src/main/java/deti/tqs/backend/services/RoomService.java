package deti.tqs.backend.services;

import java.util.ArrayList;
import java.util.List;

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

  public Room updateRoom(Room room, long roomID, long newFacilityID) throws NoSuchFieldException {

    Room found = roomRepository.findById(roomID);

    if (found == null)
      throw new EntityNotFoundException("Room does not exist");

    // Check if the name is missing
    
    if(room.getName() == null || room.getName().length() == 0) 
      throw new NoSuchFieldException("Room must have a name");

    // Check if the capacity has a valid value

    if(room.getMaxChairsCapacity() <= 0)
      throw new NoSuchFieldException("Room must have a valid capacity value greater than 0"); 

    Facility newFacility = null;

    if (newFacilityID != 0) {

      newFacility = facilityRepository.findById(newFacilityID);

      if (newFacility == null)
        throw new EntityNotFoundException("Facility does not exist");

      // Check if the facility is at full capacity

      int size = newFacility.getRooms() == null ? 0 : newFacility.getRooms().size();
      
      if(newFacility.getMaxRoomsCapacity() <= size)
        throw new IllegalStateException("Facility is at full capacity");

    }

    // Check if a room with the same name already exists in this facility

    if (!found.getName().equals(room.getName())) {

      long facID =  newFacilityID != 0 ? newFacilityID : found.getFacility().getId();

      Room roomSameName = roomRepository.findByNameAndFacilityId(room.getName(), facID);

      if (roomSameName != null)
        throw new EntityExistsException("Room with this name already exists in this facility");
        
    }
    
    found.setName(room.getName());
    found.setMaxChairsCapacity(room.getMaxChairsCapacity());
    found.setFacility(newFacility != null ? newFacility : found.getFacility());

    return roomRepository.save(found);

  }

  public Room findById(long id) {

    Room found = roomRepository.findById(id);

    if (found == null)
      throw new EntityNotFoundException("Room does not exist");

    return found;

  }

  public List<Room> findAllRooms() {
    return roomRepository.findAll();
  } 

  public Room findByName(String name) {

    Room found = roomRepository.findByName(name);

    if (found == null)
      throw new EntityNotFoundException("Room does not exist");

    return found;
  }

  public List<Room> searchByFacilityInfo(String name, long facilityID) throws Exception {

    List<Room> found = new ArrayList<>();

    // both parameters are null then throw exception

    if(name == null && facilityID == 0)
      throw new NoSuchFieldException("At least one parameter must be provided");

    // if both parameters are provided then search by both and it should only find one

    else if(name != null && facilityID != 0) {

      Room r = roomRepository.findByNameAndFacilityId(name, facilityID);

      if(r == null)
        throw new EntityNotFoundException("Room does not exist");

      found.add(r);

    // if only the name is provided then search by facility name

    } else if (name != null && facilityID == 0) {

      found = roomRepository.findByFacilityName(name);

      if (found.size() == 0)
        throw new EntityNotFoundException("Room does not exist");
    
    // else search by facility ID
    } else {

      found = roomRepository.findByFacilityId(facilityID);

      if (found.size() == 0)
        throw new EntityNotFoundException("Room does not exist");

    }

    return found;

  }

  public void deleteRoom(long roomID) {

    Room found = roomRepository.findById(roomID);

    if (found == null)
      throw new EntityNotFoundException("Room does not exist");

    roomRepository.delete(found);

  }

}
