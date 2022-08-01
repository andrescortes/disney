package co.com.disney.film.domain.dto;

import co.com.disney.film.domain.dto.responseDto.CharacterAllResponseDto;
import co.com.disney.film.domain.dto.responseDto.CharacterPartialResponseDto;
import co.com.disney.film.domain.dto.responseDto.CharacterResponseDto;
import co.com.disney.film.domain.dto.responseDto.GenreAllResponseDto;
import co.com.disney.film.domain.dto.responseDto.MovieAllResponseDto;
import co.com.disney.film.domain.dto.responseDto.MoviePartialResponseDto;
import co.com.disney.film.domain.dto.responseDto.MovieResponseDto;
import co.com.disney.film.domain.model.Character;
import co.com.disney.film.domain.model.Genre;
import co.com.disney.film.domain.model.Movie;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


public class mapper {
    //mapper to character

    //methods to filter response character
    public static CharacterResponseDto characterToCharacterResponseDto(Character character) {
        String downloadURL = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/characters/download/")
            .path(character.getCharacterId().toString())
            .toUriString();
        CharacterResponseDto characterResponseDto = new CharacterResponseDto();
        characterResponseDto.setName(character.getName());
        characterResponseDto.setImage(downloadURL);
        return characterResponseDto;
    }

    public static List<CharacterResponseDto> charactersToCharacterResponseDtos(
        List<Character> characters) {
        List<CharacterResponseDto> characterResponseDtos = new ArrayList<>();
        characters.forEach(character -> {
            characterResponseDtos.add(characterToCharacterResponseDto(character));
        });
        return characterResponseDtos;
    }

    //methods to response with all attr character
    public static CharacterAllResponseDto characterToCharacterAllResponseDto(Character character) {
        String downloadURL = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/characters/download/")
            .path(character.getCharacterId().toString())
            .toUriString();
        CharacterAllResponseDto characterAllResponseDto = new CharacterAllResponseDto();
        characterAllResponseDto.setCharacterId(character.getCharacterId());
        characterAllResponseDto.setName(character.getName());
        characterAllResponseDto.setImage(downloadURL);
        characterAllResponseDto.setAge(character.getAge());
        characterAllResponseDto.setWeight(character.getWeight());
        characterAllResponseDto.setHistory(character.getHistory());
        List<MovieAllResponseDto> movieAllResponseDtos = new ArrayList<>();
        if (character.getMovies() != null) {
            character.getMovies().forEach(movie -> {
                movieAllResponseDtos.add(movieToMovieAllResponseDto(movie));
            });
        }
        characterAllResponseDto.setMovies(movieAllResponseDtos);
        return characterAllResponseDto;
    }

    public static List<CharacterAllResponseDto> charactersToCharacterAllResponseDtos(
        List<Character> characters) {
        List<CharacterAllResponseDto> characterAllResponseDtos = new ArrayList<>();
        characters.forEach(character -> {
            characterAllResponseDtos.add(characterToCharacterAllResponseDto(character));
        });
        return characterAllResponseDtos;
    }

    public static MovieAllResponseDto movieToMovieAllResponseDto(Movie movie) {
        MovieAllResponseDto movieAllResponseDto = new MovieAllResponseDto();
        String downloadURL = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/movies/download/")
            .path(movie.getMovieId().toString())
            .toUriString();
        movieAllResponseDto.setMovieId(movie.getMovieId());
        movieAllResponseDto.setTitle(movie.getTitle());
        movieAllResponseDto.setImage(downloadURL);
        movieAllResponseDto.setRate(movie.getRate());
        movieAllResponseDto.setCreateDate(movie.getCreateDate().toString());
        movieAllResponseDto.setGenre(movie.getGenre().getName());
        return movieAllResponseDto;

    }

    //mapper to movie
    public static MovieResponseDto movieToMovieResponseDto(Movie movie) {
        MovieResponseDto movieResponseDto = new MovieResponseDto();
        String downloadURL = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/movies/download/")
            .path(movie.getMovieId().toString())
            .toUriString();
        movieResponseDto.setTitle(movie.getTitle());
        movieResponseDto.setImage(downloadURL);
        movieResponseDto.setCreatedDate(movie.getCreateDate().toString());
        return movieResponseDto;
    }

    public static List<MovieResponseDto> moviesToMovieResponseDtos(List<Movie> movies) {
        List<MovieResponseDto> movieResponseDtos = new ArrayList<>();
        movies.forEach(movie -> {
            movieResponseDtos.add(movieToMovieResponseDto(movie));
        });
        return movieResponseDtos;
    }

    public static CharacterPartialResponseDto characterToCharacterPartialResponseDto(
        Character character) {
        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/characters/download/")
            .path(character.getCharacterId().toString())
            .toUriString();
        CharacterPartialResponseDto characterPartialResponseDto = new CharacterPartialResponseDto();
        characterPartialResponseDto.setCharacterId(character.getCharacterId());
        characterPartialResponseDto.setImage(downloadURL);
        characterPartialResponseDto.setName(character.getName());
        characterPartialResponseDto.setAge(character.getAge());
        characterPartialResponseDto.setWeight(character.getWeight());
        characterPartialResponseDto.setHistory(character.getHistory());
        return characterPartialResponseDto;
    }

    public static List<CharacterPartialResponseDto> charactersToCharacterPartialResponseDtos(
        List<Character> characters) {
        List<CharacterPartialResponseDto> characterPartialResponseDtos = new ArrayList<>();
        characters.forEach(character -> characterPartialResponseDtos.add(
            characterToCharacterPartialResponseDto(character)));
        return characterPartialResponseDtos;
    }

    public static MoviePartialResponseDto movieToMoviePartialResponseDto(Movie movie) {
        MoviePartialResponseDto moviePartialResponseDto = new MoviePartialResponseDto();
        String downloadURL = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/movies/download/")
            .path(movie.getMovieId().toString())
            .toUriString();
        moviePartialResponseDto.setMovieId(movie.getMovieId());
        moviePartialResponseDto.setTitle(movie.getTitle());
        moviePartialResponseDto.setImage(downloadURL);
        moviePartialResponseDto.setRate(movie.getRate());
        moviePartialResponseDto.setCreateDate(movie.getCreateDate().toString());
        moviePartialResponseDto.setGenre(movie.getGenre().getName());
        List<CharacterPartialResponseDto> characterPartialResponseDtos = new ArrayList<>();
        movie.getCharacters().forEach(character -> characterPartialResponseDtos.add(
            characterToCharacterPartialResponseDto(character)));
        moviePartialResponseDto.setCharacters(characterPartialResponseDtos);

        return moviePartialResponseDto;
    }

    public static List<MoviePartialResponseDto> moviesToMoviePartialResponseDtos(
        List<Movie> movies) {
        List<MoviePartialResponseDto> moviePartialResponseDtos = new ArrayList<>();
        movies.forEach(
            movie -> moviePartialResponseDtos.add(movieToMoviePartialResponseDto(movie)));
        return moviePartialResponseDtos;
    }

    //mapper to Genre
    public static GenreAllResponseDto genreToGenreAllResponseDto(Genre genre) {
        GenreAllResponseDto genreAllResponseDto = new GenreAllResponseDto();
        String downloadURL = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/genres/download/")
            .path(genre.getGenreId().toString())
            .toUriString();
        genreAllResponseDto.setGenreId(genre.getGenreId());
        genreAllResponseDto.setName(genre.getName());
        genreAllResponseDto.setImage(downloadURL);
        List<MovieAllResponseDto> movieAllResponseDtos = new ArrayList<>();
        genre.getMovies()
            .forEach(movie -> movieAllResponseDtos.add(movieToMovieAllResponseDto(movie)));
        genreAllResponseDto.setMovies(movieAllResponseDtos);
        return genreAllResponseDto;
    }


    public static List<GenreAllResponseDto> genresToGenreResponseDtos(List<Genre> genres) {
        List<GenreAllResponseDto> genreAllResponseDtos = new ArrayList<>();
        genres.forEach(genre -> {
            genreAllResponseDtos.add(genreToGenreAllResponseDto(genre));
        });
        return genreAllResponseDtos;
    }

}
