package deti.tqs.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.tqs.backend.models.Chair;
import deti.tqs.backend.models.Room;
import deti.tqs.backend.repositories.ChairRepository;
import deti.tqs.backend.repositories.RoomRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ChairService {

    private ChairRepository chairRepository;

    private RoomRepository roomRepository;

    @Autowired
    ChairService(ChairRepository chairRepository, RoomRepository roomRepository) {
        this.chairRepository = chairRepository;
        this.roomRepository = roomRepository;
    }

    public Chair addChair(Chair chair, long roomID) throws Exception {

        // Check if the chair name is valid

        if (chair.getName() == null || chair.getName().length() == 0)
            throw new NoSuchFieldException("Chair must have a name");

        // Check if the room exists

        Room foundRoom = roomRepository.findById(roomID);

        if (foundRoom == null)
            throw new EntityNotFoundException("This room does not exists");

        // Check if the room is at full capacity

        int size = foundRoom.getChairs() == null ? 0 : foundRoom.getChairs().size();

        if (foundRoom.getMaxChairsCapacity() <= size)
            throw new IllegalStateException("Room is at full capacity");

        // Check if a chair with the same name already exists in this room

        Chair sameNameChair = chairRepository.findByNameAndRoomId(chair.getName(), roomID);

        if (sameNameChair != null)
            throw new EntityExistsException("Chair with this name already exists in this room");

        chair.setRoom(foundRoom);

        return chairRepository.save(chair);

    }

    public Chair getChair(long id) {
        
        Chair foundChair = chairRepository.findById(id);

        if (foundChair == null)
            throw new EntityNotFoundException("Chair not found");

        return foundChair;

    }

    public Chair getChairByName(String name) {

        Chair foundChair = chairRepository.findByName(name);

        if (foundChair == null)
            throw new EntityNotFoundException("Chair not found");

        return foundChair;
    }

    public Chair getChairByNameAndRoomID(String name, long roomID) {

        Chair foundChair = chairRepository.findByNameAndRoomId(name, roomID);

        if (foundChair == null)
            throw new EntityNotFoundException("Chair not found");

        return foundChair;
    }

    public List<Chair> getAllChairs() {
        return chairRepository.findAll();
    }

    public List<Chair> getChairsByRoomID(long roomID) {
        return chairRepository.findByRoomId(roomID);
    }

    public void deleteChair(long id) {

        Chair foundChair = chairRepository.findById(id);

        if (foundChair == null)
            throw new EntityNotFoundException("Chair not found");

        chairRepository.delete(foundChair);

    }

}
