package ro.fiipractic.mycinema.controllers;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ro.fiipractic.mycinema.dtos.CinemaDto;
import ro.fiipractic.mycinema.entities.Cinema;
import ro.fiipractic.mycinema.services.impl.CinemaServiceImpl;

import java.net.URISyntaxException;

public class CinemaControllerTest {

    @Mock
    private CinemaServiceImpl cinemaService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CinemaController controller;

    private Cinema cinema;
    private CinemaDto cinemaDto;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cinema = buildCinema();
        cinemaDto = buildCinemaDto();
    }

    @Test
    public void shouldReturnResponseEntityForSave() throws URISyntaxException {
        //arrange
        Mockito.when(cinemaService.saveCinema(cinema)).thenReturn(cinema);
        Mockito.when(modelMapper.map(cinemaDto, Cinema.class)).thenReturn(cinema);

        //act
        ResponseEntity<CinemaDto> cinemaResponseEntity = controller.saveCinema(cinemaDto);

        //assert
        Assertions.assertThat(cinemaResponseEntity).isNotNull();
        Assertions.assertThat(cinemaResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(cinemaResponseEntity.getBody()).isEqualToComparingFieldByFieldRecursively(cinema);
        Assertions.assertThat(cinemaResponseEntity.getHeaders().getLocation().toString()).isEqualTo("/api/cinemas/1");
    }

    @After
    public void tearDown() {
        cinemaDto = null;
        cinema = null;
    }

    private Cinema buildCinema() {
        Cinema cinema = new Cinema();
        cinema.setId(1L);
        cinema.setAddress("address");
        cinema.setName("cinemaName");
        return cinema;
    }

    private CinemaDto buildCinemaDto() {
        CinemaDto cinemaDto = new CinemaDto();
        cinemaDto.setId(1L);
        cinemaDto.setAddress("addressDto");
        cinemaDto.setName("cinemaNameDto");
        return cinemaDto;
    }
}
