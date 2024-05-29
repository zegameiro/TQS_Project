package deti.tqs.backend.models;

public enum BeautyService {
  
  BASIC_HAIRDRESSER(0),
  COMPLEX_HAIRDRESSER(1),
  MAKEUP(2),
  DEPILATION(3),
  MANICURE_PEDICURE(4),
  SPA(5);

  private final int id;

  BeautyService(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public static BeautyService fromId(int id) {

    for (BeautyService service : BeautyService.values()) {

      if (service.getId() == id) 
        return service;

    }

    return null;
  }

  public static BeautyService fromString(String service) {

    for (BeautyService s : BeautyService.values()) {

      if (s.toString().equals(service))
        return s;

    }
    return null;
  }

}
