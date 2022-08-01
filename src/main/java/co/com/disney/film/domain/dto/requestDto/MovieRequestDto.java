package co.com.disney.film.domain.dto.requestDto;



import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Tag(name = "Movie", description = "Representation of a Movie")
public class MovieRequestDto {

    private String title;
    private int rate;
    private Set<Long> charactersIds;
    private Long genreId;

}

