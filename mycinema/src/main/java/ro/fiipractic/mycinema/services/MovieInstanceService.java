package ro.fiipractic.mycinema.services;

import ro.fiipractic.mycinema.entities.MovieInstance;
import ro.fiipractic.mycinema.exceptions.NotFoundException;

import java.util.List;

public interface MovieInstanceService {
    List<MovieInstance> getMovieInstances();

    MovieInstance saveMovieInstance(MovieInstance movieInstance);

    MovieInstance getMovieInstanceById(Long id);

    MovieInstance updateMovieInstance(MovieInstance movieInstance);

    void deleteMovieInstance(MovieInstance movieInstance) throws NotFoundException;
}
