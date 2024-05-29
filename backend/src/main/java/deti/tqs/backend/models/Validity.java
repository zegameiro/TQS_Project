package deti.tqs.backend.models;

public enum Validity {

  PENDING(0),
  CHECKED_IN(1),
  OCCURING(2),
  WAITING_PAYMENT(3),
  PAYMENT_CONFIRMED(4);

  private final int id;

  Validity(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public static Validity fromId(int id) {
    for (Validity validity : values()) {

      if (validity.getId() == id)
        return validity;

    }
    
    return null;
  }

  public static Validity fromString(String name) {

    for (Validity validity : values()) {

      if (validity.name().equals(name))
        return validity;
        
    }

    return null;
  }

}
