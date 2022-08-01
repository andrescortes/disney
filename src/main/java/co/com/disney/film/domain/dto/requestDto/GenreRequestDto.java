package co.com.disney.film.domain.dto.requestDto;



import java.util.Set;
import lombok.Data;


@Data
public class GenreRequestDto {

    private String name;
    private Set<Long> moviesIds;

}
