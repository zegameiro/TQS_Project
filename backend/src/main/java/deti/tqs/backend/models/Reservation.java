package deti.tqs.backend.models;

import jakarta.persistence.CascadeType;
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
@Table(name = "RESERVATION")
public class Reservation {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private long timestamp;

  @Column(nullable = false, unique = true)
  private String secretCode;

  @Column(nullable = false)
  private String customerName;

  @Column(nullable = false)
  private String customerEmail; 

  @Column(nullable = false)
  private String customerPhoneNumber;

  @Column
  private int validityID = 0;

  @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
  private Speciality speciality;

  @ManyToOne
  @JoinColumn
  private Room room;

  @ManyToOne
  @JoinColumn
  private ReservationQueue reservationQueue;

  @ManyToOne
  @JoinColumn
  private Employee employee;

}
