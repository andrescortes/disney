package co.com.disney.film.domain.dto.responseDto;

import co.com.disney.film.domain.model.Movie;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
public class CharacterAllResponseDto {
    private Long characterId;
    private String name;
    private String image;
    private int age;
    private double weight;
    private String history;
    private List<MovieAllResponseDto> movies;
}
