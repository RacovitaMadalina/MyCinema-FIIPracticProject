package ro.fiipractic.mycinema.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Person customer;

    @OneToOne
    @JoinColumn(name = "movie_instance_id")
    @JsonBackReference
    private MovieInstance movieInstance;

    @NotNull
    private Integer number_of_tickets;

}
