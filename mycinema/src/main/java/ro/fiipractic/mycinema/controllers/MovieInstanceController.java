package ro.fiipractic.mycinema.controllers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fiipractic.mycinema.dtos.MovieInstanceDto;
import ro.fiipractic.mycinema.entities.Cinema;
import ro.fiipractic.mycinema.entities.MovieInstance;
import ro.fiipractic.mycinema.entities.MovieRoom;
import ro.fiipractic.mycinema.exceptions.BadRequestException;
import ro.fiipractic.mycinema.exceptions.NotFoundException;
import ro.fiipractic.mycinema.services.CinemaService;
import ro.fiipractic.mycinema.services.MovieInstanceService;
import ro.fiipractic.mycinema.services.MovieRoomService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/movie-instances")
public class MovieInstanceController {

    @Autowired
    private MovieInstanceService movieInstanceService;

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private MovieRoomService movieRoomService;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(MovieInstance.class.getName());

    /**
     * Endpoint for getting all movie instances from the table 'movie_instances'
     * @return a list of all movie instances in 'movie_instances' table
     */
    @GetMapping
    public ResponseEntity<List<MovieInstanceDto>> getMovieInstances() {
        logger.info("GET MAPPING: api/movie-instances");
        return new ResponseEntity<>(movieInstanceService.getMovieInstances().stream()
                                                        .map(movieInstance -> modelMapper.map(movieInstance, MovieInstanceDto.class))
                                                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Endpoint for getting a specific movie instance based on its id
     * @param id the id of the movie instance whose infos have to be displayed
     * @throws NotFoundException when the id of the movie-instance given as path variable wasn't found in the DB
     * @return the movie instance with the id given as param
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<MovieInstanceDto> getMovieInstanceById(@PathVariable(name = "id") Long id) throws NotFoundException {
        logger.info("GET MAPPING: api/movie-instances/" + id);
        MovieInstance movieInstanceDB = movieInstanceService.getMovieInstanceById(id);
        if(movieInstanceDB == null){
            throw new NotFoundException(String.format("The movie instance with the id=%s was not found in the DB.", id));
        }
        return new ResponseEntity<>(modelMapper.map(movieInstanceDB, MovieInstanceDto.class), HttpStatus.OK);
    }

    /**
     * Endpoint for inserting a new movie instance in the database, in its correponding table
     * @param movieInstanceDto The DTO with the completed fields corresponding to the new movie instance
     * @return the location of the new created movie instance
     * @throws URISyntaxException
     * @throws NotFoundException when the cinema_id form the movieInstanceDto wasn't found in the DB
     * @throws NotFoundException when the movie_room_id form the movieInstanceDto wasn't found in the DB
     * @throws BadRequestException when the movie_room whose id was given in the body doesn't belong to the cinema given in body
     */
    @PostMapping
    public ResponseEntity<MovieInstanceDto> createMovieInstance(@RequestBody MovieInstanceDto movieInstanceDto) throws URISyntaxException, NotFoundException, BadRequestException {
        logger.info("POST MAPPING: api/movie-instances/");
        Cinema cinemaDB = cinemaService.getCinemaById(movieInstanceDto.getCinema_id());
        MovieRoom movieRoomDB = movieRoomService.getMovieRomById(movieInstanceDto.getMovie_room_id());

        if (cinemaDB == null) {
            throw new NotFoundException(String.format("Cinema with id=%s was not found in Database.", movieInstanceDto.getCinema_id()));
        }

        if (movieRoomDB == null) {
            throw new NotFoundException(String.format("Movie room with id=%s was not found in Database.", movieInstanceDto.getMovie_room_id()));
        }

        MovieInstance mappedMovieInstance = modelMapper.map(movieInstanceDto, MovieInstance.class);
        if (cinemaDB.getId() != movieRoomDB.getCinema().getId()) {
            throw new BadRequestException(String.format("Movie room with id=%s does not belong to the cinema with id=%s",
                                                        movieInstanceDto.getMovie_room_id(),
                                                        movieInstanceDto.getCinema_id()));
        }

        MovieInstance savedMovieInstance = movieInstanceService.saveMovieInstance(mappedMovieInstance);
        return ResponseEntity.created(new URI("/api/movie-instances/" + savedMovieInstance.getId()))
                             .body(modelMapper.map(savedMovieInstance, MovieInstanceDto.class));
    }

    /**
     * Endpoint for updating a movie instance whose id was given as path variable
     * @param id the id of the movie instance that has t be updated
     * @param movieInstanceDtoToBeUdated the body containing the fields (with modifications) of the movie instance with the given param id
     * @return the corresponding movie instance whose fields were now updated
     * @throws NotFoundException when the id given as path variable is
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<MovieInstanceDto> updatemovieInstance(@PathVariable("id") Long id, @RequestBody MovieInstanceDto movieInstanceDtoToBeUdated) throws NotFoundException {
        logger.info("PUT MAPPING: api/movie-instances/" + id);
        MovieInstance movieInstanceDB = movieInstanceService.getMovieInstanceById(id);
        if (movieInstanceDB == null) {
            throw new NotFoundException(String.format("The movie instance with id=%s was not found in the database.", id));
        }
        MovieInstance mappedMovieInstanceToBeUpdated = modelMapper.map(movieInstanceDtoToBeUdated, MovieInstance.class);
        modelMapper.map(mappedMovieInstanceToBeUpdated, movieInstanceDB);
        return new ResponseEntity<>(modelMapper.map(movieInstanceService.updateMovieInstance(movieInstanceDB), MovieInstanceDto.class),
                                    HttpStatus.ACCEPTED);
    }

    /**
     * Endpoint for removing a specific movie instance
     * @param id the id of the movie instance that has to be deleted
     * @throws NotFoundException when the id of the movie instance given as path variable wasn't found in the DB
     * @return no content
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteMovieInstance(@PathVariable(name = "id") Long id) throws NotFoundException, ro.fiipractic.mycinema.exceptions.NotFoundException {
        logger.info("DELETE MAPPING: api/movie-instances/" + id);
        MovieInstance movieInstanceDB = movieInstanceService.getMovieInstanceById(id);
        if (movieInstanceDB == null) {
            throw new NotFoundException(String.format("Movie instance with id=%s was not found.", id));
        }
        movieInstanceService.deleteMovieInstance(movieInstanceDB);
        return ResponseEntity.noContent().build();
    }
}