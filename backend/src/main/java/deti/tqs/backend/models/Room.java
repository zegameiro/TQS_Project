package deti.tqs.backend.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "ROOM")
public class Room {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private int maxChairsCapacity;

  @Column(nullable = false)
  private int beautyServiceId;
  
  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Chair> chairs;

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Reservation> reservations;

  @ManyToOne
  @JoinColumn
  private Facility facility;

}
