package deti.tqs.backend.dtos;

import jakarta.validation.Valid;

public record FacilitySchema(

  @Valid String name,
  @Valid String city,
  @Valid String streetName,
  @Valid String postalCode,
  @Valid String phoneNumber

) {}
