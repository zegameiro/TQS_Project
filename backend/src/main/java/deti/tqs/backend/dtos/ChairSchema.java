package deti.tqs.backend.dtos;

import jakarta.validation.Valid;

public record ChairSchema(

  @Valid String name,
  @Valid long roomID
  
) {}
