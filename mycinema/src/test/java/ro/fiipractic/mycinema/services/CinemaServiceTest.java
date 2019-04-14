package ro.fiipractic.mycinema.services;


import javassist.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ro.fiipractic.mycinema.entities.Cinema;
//import ro.fiipractic.mycinema.exceptions.NotFoundException;
import ro.fiipractic.mycinema.repositories.CinemaRepository;
import ro.fiipractic.mycinema.services.impl.CinemaServiceImpl;

import java.util.Optional;

import static org.junit.Assert.fail;
import static java.util.Optional.empty;

public class CinemaServiceTest {

    @Mock
    private CinemaRepository cinemaRepository;

    @InjectMocks
    private CinemaServiceImpl cinemaService;

    private Cinema cinema;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        cinema = buildCinema();
    }


    @Test
    public void shouldReturnCinemaById() throws NotFoundException {
        //arrange
        Mockito.when(cinemaRepository.findById(1L)).thenReturn(Optional.ofNullable(cinema));

        //act
        Cinema cinemaById = cinemaService.getCinemaById(1L);

        //assert
        Assertions.assertThat(cinemaById).isNotNull();
        Assertions.assertThat(cinemaById).isEqualToComparingFieldByFieldRecursively(cinema);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenCinemaById() throws NotFoundException {
        //arrange
        Mockito.when(cinemaRepository.findById(6L)).thenReturn(empty());

        //act
        Cinema cinemaById = cinemaService.getCinemaById(6L);

        //assert
        fail();
    }

    private Cinema buildCinema() {
        Cinema cinema = new Cinema();
        cinema.setId(1L);
        cinema.setAddress("address");
        cinema.setName("cinemaName");
        return cinema;
    }
}