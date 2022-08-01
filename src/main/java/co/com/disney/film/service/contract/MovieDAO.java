package co.com.disney.film.service.contract;

import co.com.disney.film.domain.dto.requestDto.MovieRequestDto;
import co.com.disney.film.domain.dto.responseDto.MoviePartialResponseDto;
import co.com.disney.film.domain.dto.responseDto.MovieResponseDto;
import co.com.disney.film.domain.model.Movie;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MovieDAO extends GenericDAO<Movie> {

    MoviePartialResponseDto addMovie(MultipartFile file, MovieRequestDto movieRequestDto)
        throws Exception;

    List<MoviePartialResponseDto> getMovies();

    Movie getMovie(Long movieId);

    MoviePartialResponseDto editMovie(Long movieId, MultipartFile file,
        MovieRequestDto movieRequestDto) throws Exception;

    void deleteMovie(Long movieId);

    MovieResponseDto getMovieByTitle(String title);

    List<MovieResponseDto> getMovieByGenreId(Long genreId);

    List<MovieResponseDto> getMoviesByOrder(String order);

    List<MovieResponseDto> filterMovies(String title, Long genreId,
        String order);

    MoviePartialResponseDto addCharacterToMovie(Long characterId, Long movieId);

    MoviePartialResponseDto removeCharacterFromMovie(Long characterId, Long movieId);

    MoviePartialResponseDto deleteMovieFromCharacter(Long characterId, Long movieId);


}
