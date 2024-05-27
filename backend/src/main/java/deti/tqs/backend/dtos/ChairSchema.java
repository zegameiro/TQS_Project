package deti.tqs.backend.dtos;

import deti.tqs.backend.configs.Generated;
import deti.tqs.backend.models.Room;
import jakarta.validation.Valid;

@Generated
public record ChairSchema(

        @Valid String name,
        @Valid boolean available,
        @Valid Room room

) {
}
