package deti.tqs.backend.models;

public enum BeautyService {
  
  HAIRDRESSER(0),
  DEPILATION(1),
  MANICURE(2),
  PEDICURE(3),
  SPA(4);

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
