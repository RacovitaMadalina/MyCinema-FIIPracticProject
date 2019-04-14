package ro.fiipractic.mycinema.controllers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fiipractic.mycinema.dtos.MovieDto;
import ro.fiipractic.mycinema.entities.Movie;
import ro.fiipractic.mycinema.exceptions.NotFoundException;
import ro.fiipractic.mycinema.services.MovieService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(MovieController.class.getName());

    /**
     * Endpoint for getting all movies from the table 'movies'
     * @return a list of all movies in 'movies' table
     */
    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovies() {
        logger.info("GET MAPPING: api/movies");
        return new ResponseEntity<>(movieService.getMovies().stream()
                                                .map(movie -> modelMapper.map(movie, MovieDto.class))
                                                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Endpoint for getting a specific movie based on its id
     * @param id the id of the movie whose infos have to be displayed
     * @throws NotFoundException when the id of the movie given as path variable wasn't found in the DB
     * @return the movie with the id given as param
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable(name = "id") Long id) throws NotFoundException {
        logger.info("GET MAPPING: api/movies/" + id);
        Movie movieDB = movieService.getMovieById(id);
        if(movieDB == null){
            throw new NotFoundException(String.format("The movie with the id=%s was not found in the DB.", id));
        }
        return new ResponseEntity<>(modelMapper.map(movieDB, MovieDto.class), HttpStatus.OK);
    }

    /**
     * Endpoint for inserting a new movie in the database
     * @param movieDto The DTO with the completed fields corresponding to the new movie
     * @return the location of the new created movie
     * @throws URISyntaxException
     */
    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto) throws URISyntaxException {
        logger.info("POST MAPPING: api/movies");
        Movie movie = movieService.saveMovie(modelMapper.map(movieDto, Movie.class));
        return ResponseEntity.created(new URI("/api/movies/" + movie.getId())).body(modelMapper.map(movie, MovieDto.class));
    }

    /**
     * Endpoint for updating a movie whose id was given as path variable
     * @param id the id of the movie that has t be updated
     * @param movieDtoToBeUdated the body containing the fields (with modifications) of the movie with the given param id
     * @return the corresponding movie whose fields were now updated
     * @throws NotFoundException when the id given as path variable is
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<MovieDto> updatemovie(@PathVariable("id") Long id, @RequestBody MovieDto movieDtoToBeUdated) throws NotFoundException {
        logger.info("PUT MAPPING: api/movies/" + id);
        Movie movieDB = movieService.getMovieById(id);
        if (movieDB == null) {
            throw new NotFoundException(String.format("The movie with id=%s was not found in the database.", id));
        }
        Movie mappedMovieToBeUpdated = modelMapper.map(movieDtoToBeUdated, Movie.class);
        modelMapper.map(mappedMovieToBeUpdated, movieDB);
        return new ResponseEntity<>(modelMapper.map(movieService.updateMovie(movieDB), MovieDto.class), HttpStatus.ACCEPTED);
    }

    /**
     * Endpoint for removing a specific moviee
     * @param id the id of the movie that has to be deleted
     * @throws NotFoundException when the id of the movie given as path variable wasn't found in the DB
     * @return no content
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable(name = "id") Long id) throws NotFoundException {
        logger.info("DELETE MAPPING: api/movies/" + id);
        Movie movieDB = movieService.getMovieById(id);
        if (movieDB == null) {
            throw new NotFoundException(String.format("Movie with id=%s was not found.", id));
        }
        movieService.deleteMovie(movieDB);
        return ResponseEntity.noContent().build();
    }
}
