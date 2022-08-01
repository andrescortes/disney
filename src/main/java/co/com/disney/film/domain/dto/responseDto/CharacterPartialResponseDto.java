package co.com.disney.film.domain.dto.responseDto;

import java.util.List;
import lombok.Data;

@Data
public class CharacterPartialResponseDto {
    private Long characterId;
    private String name;
    private String image;
    private int age;
    private double weight;
    private String history;
}
