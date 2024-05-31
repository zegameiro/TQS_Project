package deti.tqs.backend.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "FACILITY")
public class Facility {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String streetName;

  @Column(nullable = false)
  private String postalCode;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private int maxRoomsCapacity;

  @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Room> rooms;

  private long reservationQueueId;

  @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Employee> employees;

}
