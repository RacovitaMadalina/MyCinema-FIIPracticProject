package ro.fiipractic.mycinema.controllers;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fiipractic.mycinema.dtos.ReservationDto;
import ro.fiipractic.mycinema.entities.Person;
import ro.fiipractic.mycinema.entities.Reservation;
import ro.fiipractic.mycinema.exceptions.BadRequestException;
import ro.fiipractic.mycinema.exceptions.NotFoundException;
import ro.fiipractic.mycinema.services.PersonService;
import ro.fiipractic.mycinema.services.ReservationService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/reservations")
public class ReservationControlller {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PersonService personService;

    private static final Logger logger = LogManager.getLogger(ReservationControlller.class.getName());

    /**
     * Endpoint for getting all reservations from the table 'reservations'
     * @return a list of all reservations in 'reservations' table
     */
    @GetMapping
    public ResponseEntity<List<ReservationDto>> getReservations() {
        logger.info("GET MAPPING: api/reservations");
        return new ResponseEntity<>(reservationService.getReservations().stream()
                                                      .map(reservation -> modelMapper.map(reservation, ReservationDto.class))
                                                      .collect(Collectors.toList()),
                                    HttpStatus.OK);
    }

    /**
     * Endpoint for getting a specific reservation based on its id
     * @param id the id of the reservation whose infos have to be displayed
     * @throws NotFoundException when the id of the reservation given as path variable wasn't found in the DB
     * @return the reservation with the id given as param
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable(name = "id") Long id) throws NotFoundException {
        logger.info("GET MAPPING: api/reservations/" + id);
        Reservation reservationDB = reservationService.getReservationById(id);
        if(reservationDB == null){
            throw new NotFoundException(String.format("The reservation with the id=%s was not found in the DB.", id));
        }
        return new ResponseEntity<>(modelMapper.map(reservationDB, ReservationDto.class), HttpStatus.OK);
    }

    /**
     * Enpoint for filtering the reservations by being given a customer_id as request param
     * @param customer_id the customer_id whose reservations have to be displayed
     * @return a list of customer's reservations
     * @throws NotFoundException when the customer_id was not found in the database
     */
    @GetMapping(value = "/filter/customers")
    public  ResponseEntity<List<ReservationDto>> getReservationsByCustomerId(@RequestParam(name = "customer_id") Long customer_id) throws NotFoundException {
        logger.info("GET MAPPING: api/reservations/filter/customers");
        Person personDB = personService.getPersonById(customer_id);
        if(personDB == null){
            throw new NotFoundException(String.format("The person with the id=%s was not found in the database.", customer_id));
        }
        List<Reservation> customersReservations = reservationService.getReservationsByCustormerId(customer_id);
        return new ResponseEntity<>(customersReservations.stream()
                                                         .map(reservation -> modelMapper.map(reservation, ReservationDto.class))
                                                         .collect(Collectors.toList()),
                                    HttpStatus.OK);
    }

    /**
     * Endpoint for inserting a new reservation in the database
     * @param reservationDto The DTO with the completed fields corresponding to the new reservation
     * @return the location of the new created reservation
     * @throws URISyntaxException
     * @throws BadRequestException when the number_of_tickets given in the reservationDto is 0 or bigger than 15
     */
    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationDto reservationDto) throws URISyntaxException, BadRequestException {
        logger.info("POST MAPPING: api/reservations");
        Reservation mappedReservation = modelMapper.map(reservationDto, Reservation.class);
        if ((mappedReservation.getNumber_of_tickets() == 0) || (mappedReservation.getNumber_of_tickets() > 15)){
            throw new BadRequestException(String.format("The ordered number of tickets should have its value between 1 and 15, not %s",
                                                        reservationDto.getNumber_of_tickets()));
        }

        Reservation savedReservation = reservationService.saveReservation(mappedReservation);
        return ResponseEntity.created(new URI("/api/reservations/" + savedReservation.getId()))
                             .body(modelMapper.map(savedReservation, ReservationDto.class));
    }

    /**
     * Endpoint for updating a reservation whose id was given as path variable
     * @param id the id of the reservation that has t be updated
     * @param reservationDtoToBeUdated the body containing the fields (with modifications) of the reservation with the given param id
     * @return the corresponding reservation whose fields were now updated
     * @throws NotFoundException when the id given as path variable is
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<ReservationDto> updatereservation(@PathVariable("id") Long id, @RequestBody Reservation reservationDtoToBeUdated) throws NotFoundException {
        logger.info("PUT MAPPING: api/reservations/" + id);
        Reservation reservationDB = reservationService.getReservationById(id);
        if (reservationDB == null) {
            throw new NotFoundException(String.format("The movie instance with id=%s was not found in the database.", id));
        }
        Reservation mappedReservationToBeUpdated = modelMapper.map(reservationDtoToBeUdated, Reservation.class);
        modelMapper.map(mappedReservationToBeUpdated, reservationDB);
        return new ResponseEntity<>(modelMapper.map(reservationService.updateReservation(reservationDB),ReservationDto.class), HttpStatus.ACCEPTED);
    }
    
    /**
     * Endpoint for removing a specific reservation
     * @param id the id of the reservation that has to be removed from DB
     * @throws NotFoundException when the id of the reservation given as path variable wasn't found in the DB
     * @return no content
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable(name = "id") Long id) throws NotFoundException {
        logger.info("DELETE MAPPING: api/reservations/" + id);
        Reservation reservationDB = reservationService.getReservationById(id);
        if (reservationDB == null) {
            throw new NotFoundException(String.format("Movie instance with id=%s was not found.", id));
        }
        reservationService.deleteReservation(reservationDB);

        return ResponseEntity.noContent().build();
    }
}
