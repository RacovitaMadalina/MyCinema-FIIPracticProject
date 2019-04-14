package ro.fiipractic.mycinema.controllers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fiipractic.mycinema.dtos.MovieRoomDto;
import ro.fiipractic.mycinema.entities.Cinema;
import ro.fiipractic.mycinema.entities.MovieRoom;
import ro.fiipractic.mycinema.exceptions.NotFoundException;
import ro.fiipractic.mycinema.services.CinemaService;
import ro.fiipractic.mycinema.services.MovieRoomService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/movie-rooms")
public class MovieRoomController {

    @Autowired
    private MovieRoomService movieRoomService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CinemaService cinemaService;

    private static final Logger logger = LogManager.getLogger(MovieRoomController.class.getName());

    /**
     * Endpoint for getting all movie rooms from the table 'movie_rooms'
     * @return a list of all movie rooms in 'movie_rooms' table
     */
    @GetMapping
    public ResponseEntity<List<MovieRoomDto>> getAllMovieRooms() {
        logger.info("GET MAPPING: api/movie-rooms");
        return new ResponseEntity<>(movieRoomService.getMovieRooms().stream()
                                                    .map(movieRoom -> modelMapper.map(movieRoom, MovieRoomDto.class))
                                                    .collect(Collectors.toList()),
                                    HttpStatus.OK);
    }

    /**
     * Endpoint for inserting a new movie-room in the database
     * @param movieRoomDto The DTO with the completed fields corresponding to the new movie-room
     * @return the location of the new created movie-room
     * @throws URISyntaxException
     */
    @PostMapping
    public ResponseEntity<MovieRoomDto> saveMovieRoom(@RequestBody MovieRoomDto movieRoomDto) throws URISyntaxException {
        logger.info("POST MAPPING: api/movie-rooms");
        MovieRoom movieRoom = movieRoomService.saveMovieRoom(modelMapper.map(movieRoomDto, MovieRoom.class));
        return ResponseEntity.created(new URI("/api/movie-rooms/" + movieRoom.getId()))
                             .body(modelMapper.map(movieRoom, MovieRoomDto.class));
    }

    /**
     * Endpoint for getting a specific movie room based on its id
     * @param id the id of the movie room whose infos have to be displayed
     * @throws NotFoundException when the id of the movie room given as path variable wasn't found in the DB
     * @return the movie room with the id given as param
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<MovieRoomDto> getMovieRoomById(@PathVariable(name = "id") Long id) throws NotFoundException {
        logger.info("GET MAPPING: api/movie-rooms/" + id);
        MovieRoom movieRoomDB = movieRoomService.getMovieRomById(id);
        if(movieRoomDB == null){
            throw new NotFoundException(String.format("The movie room with the id=%s was not found in the DB.", id));
        }
        return new ResponseEntity<>(modelMapper.map(movieRoomDB, MovieRoomDto.class), HttpStatus.OK);
    }

    /**
     * Endpoint for filtering mmovie-rooms by cinema id
     * @param cinemaId the cinema id whose movie rooms have to be displayed
     * @throws NotFoundException if the cinema id given as request param wasn't found in the database
     * @return a list of all movie rooms belonging to the cinema whose id was given as request param
     */
    @GetMapping(value = "/filter/cinema")
    public ResponseEntity<List<MovieRoomDto>> getAllMovieRoomsByCinemaId(@RequestParam(name = "cinema_id") Long cinemaId) throws NotFoundException {
        logger.info("GET MAPPING: api/movie-rooms/filter/cinema");
        Cinema cinemaDB = cinemaService.getCinemaById(cinemaId);
        if(cinemaDB == null){
            throw new NotFoundException(String.format("The cinema with the id=%s was not found in the DB.", cinemaId));
        }
        return new ResponseEntity<>(movieRoomService.getMovieRoomsByCinema_id(cinemaId)
                               .stream()
                               .map(movieRoom -> modelMapper.map(movieRoom, MovieRoomDto.class))
                               .collect(Collectors.toList()),
                                HttpStatus.OK);
    }

    /**
     * Endpoint for updating a movie room whose id was given as path variable
     * @param id the id of the movie room that has t be updated
     * @param movieRoomDtoToBeUdated the body containing the fields (with modifications) of the movie room with the given param id
     * @return the corresponding movie room whose fields were now updated
     * @throws NotFoundException when the id given as path variable is
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<MovieRoomDto> updatemovieRoom(@PathVariable("id") Long id, @RequestBody MovieRoom movieRoomDtoToBeUdated) throws NotFoundException {
        logger.info("PUT MAPPING: api/movie-rooms/" + id);
        MovieRoom movieRoomDB = movieRoomService.getMovieRomById(id);
        if (movieRoomDB == null) {
            throw new NotFoundException(String.format("The movie instance with id=%s was not found in the database.", id));
        }
        MovieRoom mappedMovieRoomToBeUpdated = modelMapper.map(movieRoomDtoToBeUdated, MovieRoom.class);
        modelMapper.map(mappedMovieRoomToBeUpdated, movieRoomDB);
        return new ResponseEntity<>(modelMapper.map(movieRoomService.updateMovieRoom(movieRoomDB), MovieRoomDto.class), HttpStatus.ACCEPTED);
    }

    /**
     * Endpoint for removing a specific movie room
     * @param id the id of the movie room that has to be deleted
     * @throws NotFoundException when the id of the movie room given as path variable wasn't found in the DB
     * @return no content
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteMovieRoom(@PathVariable(name = "id") Long id) throws NotFoundException{
        logger.info("DELETE MAPPING: api/movie-rooms/" + id);
        MovieRoom movieRoomDB = movieRoomService.getMovieRomById(id);
        if(movieRoomDB == null){
            throw new NotFoundException(String.format("MovieRoom with id=%s was not found.", id));
        }
        movieRoomService.deleteMovieRoom(movieRoomDB);
        return ResponseEntity.noContent().build();
    }
}
