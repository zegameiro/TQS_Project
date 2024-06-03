package deti.tqs.backend.dtos;

import deti.tqs.backend.configs.Generated;
import jakarta.validation.Valid;

@Generated
public record SpecialitySchema(
    @Valid String name,
    @Valid double price,
    @Valid int beautyServiceId
) {}
