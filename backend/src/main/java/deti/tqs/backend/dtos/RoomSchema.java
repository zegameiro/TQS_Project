package deti.tqs.backend.dtos;

import deti.tqs.backend.configs.Generated;
import jakarta.validation.Valid;

@Generated
public record RoomSchema(

  @Valid String name,
  @Valid int maxChairsCapacity,
  @Valid long facilityID,
  @Valid String beautyServiceID

) {}
