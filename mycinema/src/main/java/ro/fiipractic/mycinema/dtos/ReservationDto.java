package ro.fiipractic.mycinema.dtos;

import lombok.Data;

@Data
public class ReservationDto {

    private Long id;

    private Long customer_id;

    private Long movie_instance_id;

    private Integer number_of_tickets;

}
