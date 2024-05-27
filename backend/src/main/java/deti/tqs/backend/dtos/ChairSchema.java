package deti.tqs.backend.dtos;

import jakarta.validation.Valid;
import deti.tqs.backend.configs.Generated;

@Generated
public record ChairSchema(

  @Valid String name,
  @Valid long roomID
  
) {}
