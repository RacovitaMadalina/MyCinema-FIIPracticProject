package ro.fiipractic.mycinema.dtos;

import lombok.Data;

@Data
public class MovieRoomDto {

    private Long id;

    private String name;

    private Integer capacity;

    private Long cinema_id;
}
