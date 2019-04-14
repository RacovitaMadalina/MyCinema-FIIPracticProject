package ro.fiipractic.mycinema.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.fiipractic.mycinema.entities.MovieInstance;
import ro.fiipractic.mycinema.entities.Person;
import ro.fiipractic.mycinema.entities.Reservation;
import ro.fiipractic.mycinema.exceptions.NotFoundException;
import ro.fiipractic.mycinema.repositories.MovieInstanceRepository;
import ro.fiipractic.mycinema.repositories.PersonRepository;
import ro.fiipractic.mycinema.repositories.ReservationRepository;
import ro.fiipractic.mycinema.services.ReservationService;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MovieInstanceRepository movieInstanceRepository;

    @Override
    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    @Override
    public Reservation updateReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getReservationsByCustormerId(Long customerId) {
        return reservationRepository.getReservationByCustomer_Id(customerId);
    }

    @Override
    public void deleteReservation(Reservation reservation) throws NotFoundException{
        Long personId = reservation.getCustomer().getId();
        Person person = personRepository.findById(personId)
                                        .orElseThrow(() -> new NotFoundException(String.format("Person with id=%s was not found.", personId)));

        Long movieInstanceId = reservation.getMovieInstance().getId();
        MovieInstance movieInstance = movieInstanceRepository.findById(movieInstanceId)
                                                                 .orElseThrow(() -> new NotFoundException(String.format("Movie Instance with id=%s was not found.", movieInstanceId)));

        person.getReservations().remove(reservation);
        personRepository.save(person);

        movieInstance.getReservations().remove(reservation);
        movieInstanceRepository.save(movieInstance);

        reservationRepository.delete(reservation);
    }
}
