package deti.tqs.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  private Long id;

  @Column(nullable = false)
  private long timestamp;

  @Column(nullable = false)
  private Validity validity;

  @Column(nullable = false)
  private String speciality;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Facility facility;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Customer costumer;

}
