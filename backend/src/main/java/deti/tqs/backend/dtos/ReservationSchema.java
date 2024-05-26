package deti.tqs.backend.dtos;

import deti.tqs.backend.configs.Generated;
import deti.tqs.backend.models.Customer;
import jakarta.validation.Valid;

@Generated
public record ReservationSchema(

        @Valid String speciality,
        @Valid long roomID,
        @Valid Customer customer) {
}
