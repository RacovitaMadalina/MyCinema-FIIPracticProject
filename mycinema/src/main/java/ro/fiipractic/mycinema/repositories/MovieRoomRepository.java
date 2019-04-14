package ro.fiipractic.mycinema.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fiipractic.mycinema.entities.MovieRoom;

import java.util.List;

public interface MovieRoomRepository extends JpaRepository<MovieRoom, Long> {
    List<MovieRoom> getMovieRoomByCinema_Id(Long cinemaId); //named queries
}
