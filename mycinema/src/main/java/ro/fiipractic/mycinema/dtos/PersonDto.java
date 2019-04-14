package ro.fiipractic.mycinema.dtos;

import lombok.Data;

@Data
public class PersonDto {

    private Long id;

    private String fullName;

    private String phone;

    private String email;
}
