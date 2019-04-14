package ro.fiipractic.mycinema.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.fiipractic.mycinema.entities.Cinema;
import ro.fiipractic.mycinema.entities.Movie;
import ro.fiipractic.mycinema.entities.MovieInstance;
import ro.fiipractic.mycinema.entities.MovieRoom;
import ro.fiipractic.mycinema.exceptions.NotFoundException;
import ro.fiipractic.mycinema.repositories.CinemaRepository;
import ro.fiipractic.mycinema.repositories.MovieInstanceRepository;
import ro.fiipractic.mycinema.repositories.MovieRepository;
import ro.fiipractic.mycinema.repositories.MovieRoomRepository;
import ro.fiipractic.mycinema.services.MovieInstanceService;

import java.util.List;

@Service
public class MovieInstanceServiceImpl implements MovieInstanceService {

    @Autowired
    private MovieInstanceRepository movieInstanceRepository;

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieRoomRepository movieRoomRepository;

    @Override
    public List<MovieInstance> getMovieInstances() {
        return movieInstanceRepository.findAll();
    }

    @Override
    public MovieInstance saveMovieInstance(MovieInstance movieInstance) {
        return movieInstanceRepository.save(movieInstance);
    }

    @Override
    public MovieInstance getMovieInstanceById(Long id) {
        return movieInstanceRepository.findById(id).orElse(null);
    }

    @Override
    public MovieInstance updateMovieInstance(MovieInstance movieInstance) {
        return movieInstanceRepository.save(movieInstance);
    }

    @Override
    public void deleteMovieInstance(MovieInstance movieInstance) throws NotFoundException{
        Long cinemaId = movieInstance.getCinema().getId(); // for avoiding multiple DB calls
        Cinema cinema = cinemaRepository.findById(cinemaId)
                                        .orElseThrow(() -> new NotFoundException(String.format("Cinema with id=%s was not found.", cinemaId)));

        Long movieId = movieInstance.getMovie().getId();
        Movie movie = movieRepository.findById(movieInstance.getMovie().getId())
                                     .orElseThrow(() -> new NotFoundException(String.format("Movie with id=%s was not found.", movieId)));

        Long movieRoomId = movieInstance.getMovieRoom().getId();
        MovieRoom movieRoom = movieRoomRepository.findById(movieInstance.getMovieRoom().getId())
                                                 .orElseThrow(() -> new NotFoundException(String.format("MovieRoom with id=%s was not found.", movieRoomId)));

        cinema.getMovieInstances().remove(movieInstance);
        cinemaRepository.save(cinema);

        movie.getMovieInstances().remove(movieInstance);
        movieRepository.save(movie);

        movieRoom.getMovieInstances().remove(movieInstance);
        movieRoomRepository.save(movieRoom);

        movieInstanceRepository.delete(movieInstance);
    }
}
