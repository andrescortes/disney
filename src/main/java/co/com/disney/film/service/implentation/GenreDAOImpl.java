package co.com.disney.film.service.implentation;

import co.com.disney.film.domain.dto.mapper;
import co.com.disney.film.domain.dto.requestDto.GenreRequestDto;
import co.com.disney.film.domain.dto.responseDto.GenreAllResponseDto;
import co.com.disney.film.domain.model.Genre;
import co.com.disney.film.domain.model.Image;
import co.com.disney.film.domain.model.Movie;
import co.com.disney.film.repository.GenreRepository;
import co.com.disney.film.repository.MovieRepository;
import co.com.disney.film.service.contract.GenreDAO;

import co.com.disney.film.util.Utils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class GenreDAOImpl extends GenericDAOImpl<Genre, GenreRepository> implements GenreDAO {


    private final MovieRepository movieRepository;

    public GenreDAOImpl(GenreRepository repository,
        MovieRepository movieRepository) {
        super(repository);
        this.movieRepository = movieRepository;
    }

    @Transactional
    @Override
    public GenreAllResponseDto addGenre(MultipartFile file, GenreRequestDto genreRequestDto)
        throws Exception {
        Genre genre = new Genre();
        genre.setName(genreRequestDto.getName());
        genre.setImage(Utils.setUpImageOrThrowException(file));
        Genre genreSaved = repository.save(genre);
        if (genreRequestDto.getMoviesIds().size() > 0) {
            genreRequestDto.getMoviesIds().forEach(movieId -> {
                addMovieToGenre(movieId, genreSaved.getGenreId());
            });
        }
        return mapper.genreToGenreAllResponseDto(repository.save(genreSaved));
    }

    @Transactional(readOnly = true)
    @Override
    public List<GenreAllResponseDto> getGenres() {
        List<Genre> genres = StreamSupport.stream(repository.findAll().spliterator(), false)
            .collect(Collectors.toList());
        return mapper.genresToGenreResponseDtos(genres);
    }

    @Transactional(readOnly = true)
    @Override
    public Genre getGenre(Long genreId) {
        Genre genre = repository.findById(genreId).orElseThrow(() ->
            new IllegalArgumentException("Genre with id: " + genreId + " could not be found."));
        return genre;
    }

    @Transactional
    @Override
    public void addMovieToGenre(Long movieId, Long genreId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() ->
            new IllegalArgumentException("Movie no exist."));
        Genre genre = getGenre(genreId);
        movie.setGenre(genre);
        genre.getMovies().add(movie);
        repository.save(genre);
    }

    @Transactional
    @Override
    public void deleteGenre(Long genreId) {
        Genre genre = getGenre(genreId);
        if (genre.getMovies().size() > 0) {
            genre.getMovies().forEach(movie -> {
                movie.setGenre(null);
                genre.getMovies().remove(movie);
            });
            repository.save(genre);
        }
        repository.delete(genre);
    }

}
