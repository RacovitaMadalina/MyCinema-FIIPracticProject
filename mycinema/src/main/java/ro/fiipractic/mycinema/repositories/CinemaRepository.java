package ro.fiipractic.mycinema.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.fiipractic.mycinema.entities.Cinema;

import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    @Query("SELECT c FROM Cinema c JOIN c.movieRooms WHERE capacity = :capacity") // HQL queries
    public List<Cinema> getCinemasByMovieRoomCapacity(@Param("capacity") Integer capacity);
}
