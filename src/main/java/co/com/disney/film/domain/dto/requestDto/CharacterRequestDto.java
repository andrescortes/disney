package co.com.disney.film.domain.dto.requestDto;


import java.util.Set;
import lombok.Data;

@Data
public class CharacterRequestDto {

    private String name;
    private String image;
    private int age;
    private double weight;
    private String history;
    private Set<Long> moviesIds;
}
