package ro.fiipractic.mycinema.services;

import ro.fiipractic.mycinema.entities.Reservation;
import ro.fiipractic.mycinema.exceptions.NotFoundException;

import java.util.List;

public interface ReservationService {

    List<Reservation> getReservations();

    Reservation saveReservation(Reservation reservation);

    Reservation getReservationById(Long id);

    Reservation updateReservation(Reservation reservation);

    List<Reservation> getReservationsByCustormerId(Long customerId);

    void deleteReservation(Reservation reservation) throws NotFoundException;
}
