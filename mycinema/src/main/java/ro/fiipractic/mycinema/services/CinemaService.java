package ro.fiipractic.mycinema.services;


import ro.fiipractic.mycinema.entities.Cinema;

import java.util.List;
import java.util.Optional;

public interface CinemaService {
    List<Cinema> getCinemas();

    Cinema saveCinema(Cinema cinema);

    Cinema getCinemaById(Long id);

    List<Cinema> getCinemaByMovieRoomCapacity(Integer capacity);

    Cinema updateCinema(Cinema cinema);

    void deleteCinema(Cinema cinema);
}
