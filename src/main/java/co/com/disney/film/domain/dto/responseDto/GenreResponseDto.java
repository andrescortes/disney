package co.com.disney.film.domain.dto.responseDto;

import co.com.disney.film.domain.model.Movie;
import java.util.List;
import lombok.Data;

@Data
public class GenreResponseDto {

    private String name;
    private String image;
    private List<Movie> movies;
}
