package co.com.disney.film.domain.dto.responseDto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class GenreAllResponseDto {
    private Long genreId;
    private String name;
    private String image;
    private List<MovieAllResponseDto> movies;

}
