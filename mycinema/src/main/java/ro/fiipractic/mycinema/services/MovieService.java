package ro.fiipractic.mycinema.services;

import org.springframework.stereotype.Service;
import ro.fiipractic.mycinema.entities.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> getMovies();

    Movie saveMovie(Movie movie);

    Movie getMovieById(Long id);

    Movie updateMovie(Movie movie);

    void deleteMovie(Movie movie);
}
