package deti.tqs.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "SPECIALITY")
public class Speciality {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;  

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private double price;

  @Column(nullable = false)
  private int beautyServiceId;

  @OneToOne
  @JoinColumn
  @JsonIgnore
  private Reservation reservation;

  @ManyToOne
  @JoinColumn
  private Employee employee;
  
}
