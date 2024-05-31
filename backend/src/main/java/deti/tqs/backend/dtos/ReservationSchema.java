package deti.tqs.backend.dtos;

import deti.tqs.backend.configs.Generated;
import jakarta.validation.Valid;

@Generated
public record ReservationSchema(

        @Valid String timestamp,
        @Valid String secretCode,
        @Valid String customerName,
        @Valid String customerEmail,
        @Valid String customerPhoneNumber,
        @Valid String specialityID,
        @Valid String roomID

) {}
