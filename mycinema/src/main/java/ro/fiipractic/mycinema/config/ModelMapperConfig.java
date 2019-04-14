package ro.fiipractic.mycinema.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.fiipractic.mycinema.dtos.*;
import ro.fiipractic.mycinema.entities.*;

import java.util.Date;
import java.util.List;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.typeMap(MovieRoomDto.class, MovieRoom.class).addMappings(m ->{
            m.<Long>map(MovieRoomDto::getCinema_id, (MovieRoom, v) -> MovieRoom.getCinema().setId(v));
        });

        modelMapper.typeMap(ReservationDto.class, Reservation.class).addMappings(m ->{
            m.<Long>map(ReservationDto::getCustomer_id, (Reservation, a) -> Reservation.getCustomer().setId(a));
            m.<Long>map(ReservationDto::getMovie_instance_id, (Reservation, b) -> Reservation.getMovieInstance().setId(b));
        });

        modelMapper.typeMap(MovieInstanceDto.class, MovieInstance.class).addMappings(m ->{
            m.<Long>map(MovieInstanceDto::getMovie_id, (MovieInstance, a) -> MovieInstance.getMovie().setId(a));
            m.<Long>map(MovieInstanceDto::getCinema_id, (MovieInstance, b) -> MovieInstance.getCinema().setId(b));
            m.<Long>map(MovieInstanceDto::getMovie_room_id, (MovieInstance, c) -> MovieInstance.getMovieRoom().setId(c));
        });

        modelMapper.typeMap(Cinema.class, CinemaDto.class).addMappings(m ->{
            m.<Long>map(Cinema::getId, CinemaDto::setId);
            m.<String>map(Cinema::getAddress, CinemaDto::setAddress);
            m.<String>map(Cinema::getName, CinemaDto::setName);
        });

        modelMapper.typeMap(Movie.class, MovieDto.class).addMappings(m ->{
            m.<Long>map(Movie::getId, MovieDto::setId);
            m.<String>map(Movie::getTitle, MovieDto::setTitle);
            m.<String>map(Movie::getDescription, MovieDto::setDescription);
            m.<Integer>map(Movie::getDuration_minutes, MovieDto::setDuration_minutes);
        });

        modelMapper.typeMap(MovieInstance.class, MovieInstanceDto.class).addMappings(m ->{
            m.<Long>map(MovieInstance::getId, MovieInstanceDto::setId);
            m.<Date>map(MovieInstance::getStart_date, MovieInstanceDto::setStart_date);
            m.<Date>map(MovieInstance::getEnd_date, MovieInstanceDto::setEnd_date);
            m.<Integer>map(MovieInstance::getAvailable_seats, MovieInstanceDto::setAvailable_seats);
            m.map(movieInstance -> movieInstance.getCinema().getId(),MovieInstanceDto::setCinema_id);
            m.map(movieInstance -> movieInstance.getMovie().getId(),MovieInstanceDto::setMovie_id);
            m.map(movieInstance -> movieInstance.getMovieRoom().getId(),MovieInstanceDto::setMovie_room_id);
        });

        modelMapper.typeMap(MovieRoom.class, MovieRoomDto.class).addMappings(m ->{
            m.<Long>map(MovieRoom::getId, MovieRoomDto::setId);
            m.<String>map(MovieRoom::getName, MovieRoomDto::setName);
            m.<Integer>map(MovieRoom::getCapacity, MovieRoomDto::setCapacity);
            m.map(movieRoom -> movieRoom.getCinema().getId(),MovieRoomDto::setCinema_id);
        });

        modelMapper.typeMap(Person.class, PersonDto.class).addMappings(m ->{
            m.<Long>map(Person::getId, PersonDto::setId);
            m.<String>map(Person::getFullName, PersonDto::setFullName);
            m.<String>map(Person::getEmail, PersonDto::setEmail);
            m.<String>map(Person::getPhone, PersonDto::setPhone);
        });

        modelMapper.typeMap(Reservation.class, ReservationDto.class).addMappings(m ->{
            m.<Long>map(Reservation::getId, ReservationDto::setId);
            m.map(reservation -> reservation.getCustomer().getId(),ReservationDto::setCustomer_id);
            m.map(reservation -> reservation.getMovieInstance().getId(),ReservationDto::setMovie_instance_id);
            m.<Integer>map(Reservation::getNumber_of_tickets, ReservationDto::setNumber_of_tickets);
        });
        
        return modelMapper;
    }
}