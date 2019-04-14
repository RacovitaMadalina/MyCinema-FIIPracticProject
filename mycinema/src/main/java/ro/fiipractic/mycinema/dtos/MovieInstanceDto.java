package ro.fiipractic.mycinema.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class MovieInstanceDto {

    private Long id;

    private Date start_date;

    private Date end_date;

    private Integer available_seats;

    private Long cinema_id;

    private Long movie_id;

    private Long movie_room_id;
}
