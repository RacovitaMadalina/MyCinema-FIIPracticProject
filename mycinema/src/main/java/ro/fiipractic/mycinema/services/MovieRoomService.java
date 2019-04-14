package ro.fiipractic.mycinema.services;


import ro.fiipractic.mycinema.entities.MovieRoom;
import ro.fiipractic.mycinema.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface MovieRoomService {

    List<MovieRoom> getMovieRooms();

    MovieRoom saveMovieRoom(MovieRoom movieRoom);

    MovieRoom getMovieRomById(Long id);

    MovieRoom updateMovieRoom(MovieRoom movieRoom);

    List<MovieRoom> getMovieRoomsByCinema_id(Long cinemaId);

    void deleteMovieRoom(MovieRoom movieRoom) throws NotFoundException;
}
