package deti.tqs.backend.dtos;

import deti.tqs.backend.configs.Generated;
import jakarta.validation.Valid;

@Generated
public record FacilitySchema(

  @Valid String name,
  @Valid String city,
  @Valid String streetName,
  @Valid String postalCode,
  @Valid String phoneNumber,
  @Valid int maxRoomsCapacity

) {}
