package ro.fiipractic.mycinema.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.fiipractic.mycinema.entities.Cinema;
import ro.fiipractic.mycinema.entities.MovieRoom;
import ro.fiipractic.mycinema.exceptions.NotFoundException;
import ro.fiipractic.mycinema.repositories.CinemaRepository;
import ro.fiipractic.mycinema.repositories.MovieRoomRepository;
import ro.fiipractic.mycinema.services.MovieRoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// daca vreau ca 2 servicii sa implementeze aceeasi interfata trebuie sa pun adnotarea de Qualifier
@Service
public class MovieRoomServiceImpl implements MovieRoomService {

    @Autowired
    private MovieRoomRepository movieRoomRepository;

    @Autowired
    private CinemaRepository cinemaRepository;

    @Override
    public List<MovieRoom> getMovieRooms() {
        return movieRoomRepository.findAll();
    }

    @Override
    public MovieRoom saveMovieRoom(MovieRoom movieRoom) {
        return movieRoomRepository.save(movieRoom);
    }

    @Override
    public MovieRoom getMovieRomById(Long id) {
        return movieRoomRepository.findById(id).orElse(null);
    }

    @Override
    public MovieRoom updateMovieRoom(MovieRoom movieRoom) {
        return movieRoomRepository.save(movieRoom);
    }

    @Override
    public List<MovieRoom> getMovieRoomsByCinema_id(Long cinemaId) {
        return movieRoomRepository.getMovieRoomByCinema_Id(cinemaId);
    }

    @Override
    public void deleteMovieRoom(MovieRoom movieRoom) throws NotFoundException{
        Long cinemaId = movieRoom.getCinema().getId();
        Cinema cinema = cinemaRepository.findById(cinemaId)
                                        .orElseThrow(() -> new NotFoundException(String.format("Cinema with id=%s was not found.", cinemaId)));
        cinema.getMovieRooms().remove(movieRoom);
        cinemaRepository.save(cinema);
        movieRoomRepository.delete(movieRoom);
    }
}
