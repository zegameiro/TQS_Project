package deti.tqs.backend.dtos;

import java.util.List;
import java.lang.Long;

import deti.tqs.backend.configs.Generated;
import jakarta.validation.Valid;

@Generated
public record EmployeeSchema(

        @Valid boolean isAdmin,
        @Valid String fullName,
        @Valid String email,
        @Valid String phoneNumber,
        @Valid List<Long> specialitiesID

) {
}
