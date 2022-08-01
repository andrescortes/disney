package co.com.disney.film.domain.dto.responseDto;


import java.util.List;
import lombok.Data;

@Data
public class MoviePartialResponseDto {

    private Long movieId;
    private String image;
    private String title;
    private String createDate;
    private Integer rate;
    private List<CharacterPartialResponseDto> characters;
    private String genre;
}
