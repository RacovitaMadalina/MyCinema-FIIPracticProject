package ro.fiipractic.mycinema.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "movie_instance")
public class MovieInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date start_date;

    private Date end_date;

    private Integer available_seats;

    @OneToOne
    @JoinColumn(name = "cinema_id")
    @JsonBackReference
    private Cinema cinema;

    @OneToOne
    @JoinColumn(name = "movie_id")
    @JsonBackReference
    private Movie movie;

    @OneToOne
    @JoinColumn(name = "movie_room_id")
    @JsonBackReference
    private MovieRoom movieRoom;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "movieInstance")
    @JsonManagedReference
    private List<Reservation> reservations;
}
