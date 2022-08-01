package co.com.disney.film.service.contract;

import co.com.disney.film.domain.dto.requestDto.GenreRequestDto;
import co.com.disney.film.domain.dto.responseDto.GenreAllResponseDto;
import co.com.disney.film.domain.model.Genre;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface GenreDAO extends GenericDAO<Genre> {

    GenreAllResponseDto addGenre(MultipartFile file, GenreRequestDto genreRequestDto)
        throws Exception;

    List<GenreAllResponseDto> getGenres();

    Genre getGenre(Long genreId);

    void addMovieToGenre(Long movieId, Long genreId);

    void deleteGenre(Long genreId);


}
