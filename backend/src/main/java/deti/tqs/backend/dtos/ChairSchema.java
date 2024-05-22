package deti.tqs.backend.dtos;

import deti.tqs.backend.configs.Generated;
import jakarta.validation.Valid;
import deti.tqs.backend.models.Room;

@Generated
public record ChairSchema(

    @Valid String name,
    @Valid boolean available,
    @Valid Room room

) {}
