package ro.fiipractic.mycinema.dtos;

import lombok.Data;

@Data
public class MovieDto {

    private Long id;

    private String title;

    private String description;

    private Integer duration_minutes;
}
