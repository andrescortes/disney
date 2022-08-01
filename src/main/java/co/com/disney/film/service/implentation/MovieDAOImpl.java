package co.com.disney.film.service.implentation;

import co.com.disney.film.domain.dto.mapper;
import co.com.disney.film.domain.dto.requestDto.MovieRequestDto;
import co.com.disney.film.domain.dto.responseDto.MoviePartialResponseDto;
import co.com.disney.film.domain.dto.responseDto.MovieResponseDto;
import co.com.disney.film.domain.model.Character;
import co.com.disney.film.domain.model.Genre;
import co.com.disney.film.domain.model.Movie;
import co.com.disney.film.repository.CharacterRepository;
import co.com.disney.film.repository.GenreRepository;
import co.com.disney.film.repository.MovieRepository;
import co.com.disney.film.service.contract.MovieDAO;
import co.com.disney.film.util.Utils;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MovieDAOImpl extends GenericDAOImpl<Movie, MovieRepository> implements MovieDAO {

    private final CharacterRepository characterRepository;
    private final GenreRepository genreRepository;

    public MovieDAOImpl(MovieRepository repository, CharacterRepository characterRepository,
        GenreRepository genreRepository) {
        super(repository);
        this.characterRepository = characterRepository;
        this.genreRepository = genreRepository;
    }

    @Transactional
    @Override
    public MoviePartialResponseDto addMovie(MultipartFile file, MovieRequestDto movieRequestDto)
        throws Exception {
        Movie movie = new Movie();
        movie.setTitle(movieRequestDto.getTitle());
        movie.setImage(Utils.setUpImageOrThrowException(file));
        movie.setRate(movieRequestDto.getRate());
        Genre genre = genreRepository.findById(movieRequestDto.getGenreId()).orElseThrow(() ->
            new IllegalArgumentException(
                "Genre with id: " + movieRequestDto.getGenreId() + " could not be found."));
        movie.setGenre(genre);
        Movie movieSaved = repository.save(movie);
        if (movieRequestDto.getCharactersIds().size() != 0) {
            movieRequestDto.getCharactersIds().forEach(characterId -> {
                addCharacterToMovie(characterId, movieSaved.getMovieId());
            });
        }
        return mapper.movieToMoviePartialResponseDto(getMovie(movieSaved.getMovieId()));
    }


    @Transactional(readOnly = true)
    @Override
    public List<MoviePartialResponseDto> getMovies() {
        List<Movie> movies = StreamSupport
            .stream(repository.findAll().spliterator(), false)
            .collect(Collectors.toList());
        return mapper.moviesToMoviePartialResponseDtos(movies);
    }

    @Transactional(readOnly = true)
    @Override
    public Movie getMovie(Long movieId) {
        return repository.findById(movieId).orElseThrow(() ->
            new IllegalArgumentException("Movie with id: " + movieId + "could not be found."));
    }

    @Transactional
    @Override
    public MoviePartialResponseDto editMovie(Long movieId, MultipartFile file,
        MovieRequestDto movieRequestDto)
        throws Exception {
        Movie movieToEdit = getMovie(movieId);
        movieToEdit.setTitle(movieRequestDto.getTitle());
        movieToEdit.setImage(Utils.setUpImageOrThrowException(file));
        movieToEdit.setRate(movieRequestDto.getRate());
        if (movieRequestDto.getGenreId() != null) {
            movieToEdit.setGenre(
                genreRepository.findById(movieRequestDto.getGenreId()).orElseThrow(() ->
                    new IllegalArgumentException(
                        "Genre with id: " + movieRequestDto.getGenreId() + "could not be found.")));
        }
        return mapper.movieToMoviePartialResponseDto(repository.save(movieToEdit));
    }

    @Transactional
    @Override
    public void deleteMovie(Long movieId) {
        Movie movie = getMovie(movieId);
        if (movie.getCharacters().size() != 0) {
            Set<Character> characters = new CopyOnWriteArraySet<>(movie.getCharacters());
            characters.forEach(character -> {
                removeCharacterFromMovie(character.getCharacterId(), movieId);
            });
        }
        repository.delete(movie);
    }

    @Transactional(readOnly = true)
    @Override
    public MovieResponseDto getMovieByTitle(String title) {
        return mapper.movieToMovieResponseDto(repository.findMovieByTitle(title).orElseThrow(() ->
            new IllegalArgumentException("No found movie with name: " + title)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<MovieResponseDto> getMovieByGenreId(Long genreId) {
        return mapper.moviesToMovieResponseDtos(
            repository.findMovieByGenreGenreId(genreId).orElseThrow(() ->
                new IllegalArgumentException("No found movie with genreId: " + genreId)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<MovieResponseDto> getMoviesByOrder(String order) {
        Pageable sortByAsc = PageRequest.of(0, 5, Sort.by("createDate").ascending());
        Pageable sortByDesc = PageRequest.of(0, 5, Sort.by("createDate").descending());
        if (order.equals("ASC")) {
            List<Movie> movies = StreamSupport.stream(
                    repository.findAll(sortByAsc).getContent().spliterator(), false)
                .collect(Collectors.toList());
            return mapper.moviesToMovieResponseDtos(movies);
        }
        if (order.equals("DESC")) {
            List<Movie> movies = StreamSupport.stream(
                    repository.findAll(sortByDesc).getContent().spliterator(), false)
                .collect(Collectors.toList());
            return mapper.moviesToMovieResponseDtos(movies);
        }
        return null;
    }

    @Override
    public List<MovieResponseDto> filterMovies(String title, Long genreId,
        String order) {
        if (title != null) {
            return List.of(getMovieByTitle(title));
        } else if (genreId != null) {
            return getMovieByGenreId(genreId);
        } else if (order != null) {
            return getMoviesByOrder(order);
        } else {
            return mapper.moviesToMovieResponseDtos(repository.findAll());
        }
    }

    @Transactional
    @Override
    public MoviePartialResponseDto addCharacterToMovie(Long characterId, Long movieId) {
        Character character = characterRepository.findById(characterId).orElseThrow(() ->
            new IllegalArgumentException(
                "Character with id: " + characterId + "could not be found."));
        Movie movie = getMovie(movieId);
        if (movie.getCharacters().size() == 0) {
            character.getMovies().add(movie);
            movie.getCharacters().add(character);
            return mapper.movieToMoviePartialResponseDto(repository.save(movie));
        }
        if (movie.getCharacters().contains(character)) {
            throw new IllegalArgumentException("Character has already in movie.");
        }
        character.getMovies().add(movie);
        movie.getCharacters().add(character);
        return mapper.movieToMoviePartialResponseDto(repository.save(movie));
    }

    @Transactional
    @Override
    public MoviePartialResponseDto removeCharacterFromMovie(Long characterId, Long movieId) {
        Character character = characterRepository.findById(characterId).orElseThrow(() ->
            new IllegalArgumentException(
                "Character with id: " + characterId + "could not be found."));
        Movie movie = getMovie(movieId);
        if (movie.getCharacters().size() == 0) {
            throw new IllegalArgumentException("Movie haven´t characters to remove.");
        }
        if (movie.getCharacters().contains(character)) {
            character.getMovies().remove(movie);
            movie.getCharacters().remove(character);
            return mapper.movieToMoviePartialResponseDto(repository.save(movie));
        } else {
            throw new IllegalArgumentException(
                "Movie haven´t  the character with id : " + characterId + " associated.");
        }
    }

    @Override
    public MoviePartialResponseDto deleteMovieFromCharacter(Long characterId, Long movieId) {
        return removeCharacterFromMovie(characterId, movieId);
    }

}
