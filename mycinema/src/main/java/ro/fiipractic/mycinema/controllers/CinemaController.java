package ro.fiipractic.mycinema.controllers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fiipractic.mycinema.dtos.CinemaDto;
import ro.fiipractic.mycinema.entities.Cinema;
import ro.fiipractic.mycinema.exceptions.NotFoundException;
import ro.fiipractic.mycinema.services.CinemaService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/cinemas")
public class CinemaController {

    @Autowired
    CinemaService cinemaService;

    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(CinemaController.class.getName());

    /**
     * Endpoint for getting all cinemas covered in the database
     * @return a list of all cinemas in 'cinema' table
     */
    @GetMapping
    public ResponseEntity<List<CinemaDto>> getAllCinemas(){
        logger.info("GET MAPPING: api/cinemas");
        List<Cinema> cinemasList = cinemaService.getCinemas();
        return new ResponseEntity<>(cinemasList.stream()
                                               .map(cinema -> modelMapper.map(cinema, CinemaDto.class))
                                               .collect(Collectors.toList()),
                                    HttpStatus.OK);
    }

    /**
     * Endpoint for inserting a new cinema in the database
     * @param cinemaDto The DTO with the completed fields corresponding to the new cinema
     * @return the location of the new created cinema
     * @throws URISyntaxException
     */
    @PostMapping
    public ResponseEntity<CinemaDto> saveCinema(@RequestBody CinemaDto cinemaDto) throws URISyntaxException {
        logger.info("POST MAPPING: api/cinemas");
        Cinema cinema = cinemaService.saveCinema(modelMapper.map(cinemaDto, Cinema.class));
        return ResponseEntity.created(new URI("/api/cinemas/" + cinema.getId())).body(cinemaDto);
    }

    /**
     * Endpoint for getting details about a specific cinema
     * @param id The id of the cinema whose infos have to be displayed
     * @throws NotFoundException when the id of the cinema given as path variable wasn't found in the DB
     * @return the cinema whose id was given as path variable
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<CinemaDto> getCinemaById(@PathVariable(name = "id") Long id) throws NotFoundException {
        logger.info("GET MAPPING: api/cinemas/" + id);
        Cinema cinemaDB = cinemaService.getCinemaById(id);
        if(cinemaDB == null){
            throw new NotFoundException(String.format("The cinema with the id=%s was not found in the DB.", id));
        }
        return new ResponseEntity<>(modelMapper.map(cinemaDB, CinemaDto.class), HttpStatus.OK);
    }

    /**
     * Endpoint for filtering the cinemas based on movie room capacity
     * @param movieRoomCapacity the capacity of the movie room that belongs to a specific cinema
     * @return a list of cinemas which have movie rooms with the given capacity
     */
    @GetMapping(value = "/filter")
    public ResponseEntity<List<CinemaDto>> getCinemaByMovieRoomCapacity(@RequestParam(name = "movieRoomCapacity") Integer movieRoomCapacity){
        logger.info("GET MAPPING: api/cinemas/filter");
        return new ResponseEntity<>(cinemaService.getCinemaByMovieRoomCapacity(movieRoomCapacity).stream()
                                                 .map(cinema -> modelMapper.map(cinema, CinemaDto.class))
                                                 .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Endpoint for updating a cinema whose id was given as path variable
     * @param id the id of the cinema that has t be updated
     * @param cinemaDtoToBeUdated the body containing the fields (with modifications) of the cinema with the given param id
     * @return the corresponding cinema whose fields were now updated
     * @throws NotFoundException when the id given as path variable is
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<CinemaDto> updatecinema(@PathVariable("id") Long id, @RequestBody CinemaDto cinemaDtoToBeUdated) throws NotFoundException {
        logger.info("PUT MAPPING: api/cinemas/" + id);
        Cinema cinemaDB = cinemaService.getCinemaById(id);
        if (cinemaDB == null) {
            throw new NotFoundException(String.format("The cinema with id=%s was not found in the database.", id));
        }
        Cinema mappedCinemaToBeUpdated = modelMapper.map(cinemaDtoToBeUdated, Cinema.class);
        modelMapper.map(mappedCinemaToBeUpdated, cinemaDB);
        return new ResponseEntity<>(modelMapper.map(cinemaService.updateCinema(cinemaDB), CinemaDto.class), HttpStatus.ACCEPTED);
    }

    /**
     * Endpoint for removing a specific cinema
     * @param id the id of the cinema that has to be deleted
     * @throws NotFoundException when the id of the cinema given as path variable wasn't found in the DB
     * @return no content
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCinema(@PathVariable(name = "id") Long id) throws NotFoundException {
        logger.info("DELETE MAPPING: api/cinemas/" + id);
        Cinema cinemaDB = cinemaService.getCinemaById(id);
        if (cinemaDB == null) {
            throw new NotFoundException(String.format("Cinema with id=%s was not found.", id));
        }
        cinemaService.deleteCinema(cinemaDB);
        return ResponseEntity.noContent().build();
    }
}
