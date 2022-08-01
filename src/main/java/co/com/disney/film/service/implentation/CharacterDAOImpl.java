package co.com.disney.film.service.implentation;

import co.com.disney.film.util.Utils;
import co.com.disney.film.domain.dto.mapper;
import co.com.disney.film.domain.dto.requestDto.CharacterRequestDto;
import co.com.disney.film.domain.dto.responseDto.CharacterAllResponseDto;
import co.com.disney.film.domain.dto.responseDto.CharacterResponseDto;
import co.com.disney.film.domain.model.Character;
import co.com.disney.film.domain.model.Movie;
import co.com.disney.film.repository.CharacterRepository;
import co.com.disney.film.repository.MovieRepository;
import co.com.disney.film.service.contract.CharacterDAO;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CharacterDAOImpl extends GenericDAOImpl<Character, CharacterRepository> implements
    CharacterDAO {

    private final MovieRepository movieRepository;

    public CharacterDAOImpl(CharacterRepository repository, MovieRepository movieRepository) {
        super(repository);

        this.movieRepository = movieRepository;
    }

    @Transactional
    @Override
    public CharacterAllResponseDto addCharacter(MultipartFile file,
        CharacterRequestDto characterRequestDto) throws Exception {
        Character character = new Character();
        character.setName(characterRequestDto.getName());
        character.setAge(characterRequestDto.getAge());
        character.setWeight(characterRequestDto.getWeight());
        character.setHistory(characterRequestDto.getHistory());
        character.setImage(Utils.setUpImageOrThrowException(file));
        Character characterSaved = repository.save(character);
        if (characterRequestDto.getMoviesIds() != null) {
            characterRequestDto.getMoviesIds().forEach(movieId -> {
                addMovieToCharacter(movieId, characterSaved.getCharacterId());
            });
        }
        return mapper.characterToCharacterAllResponseDto(
            getCharacter(characterSaved.getCharacterId()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CharacterAllResponseDto> getCharacters() {
        return mapper.charactersToCharacterAllResponseDtos(repository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public CharacterAllResponseDto getCharacterById(Long characterId) {
        return mapper.characterToCharacterAllResponseDto(getCharacter(characterId));
    }

    @Transactional(readOnly = true)
    @Override
    public Character getCharacter(Long characterId) {
        return repository.findById(characterId).orElseThrow(() ->
            new IllegalArgumentException(
                "Character with id: " + characterId + " could not be found."));
    }

    @Transactional
    @Override
    public void deleteCharacter(Long characterId) {
        Character character = getCharacter(characterId);
        if (character.getMovies().size() != 0) {
            Set<Movie> movies = new CopyOnWriteArraySet<>(character.getMovies());
            movies.forEach(movie -> {
                removeMovieFromCharacter(movie.getMovieId(), character.getCharacterId());
            });
        }
        repository.delete(character);
    }

    @Transactional
    @Override
    public CharacterAllResponseDto editCharacter(Long characterId, MultipartFile file,
        CharacterRequestDto characterRequestDto) throws Exception {
        Character character = getCharacter(characterId);
        character.setName(characterRequestDto.getName());
        character.setAge(characterRequestDto.getAge());
        character.setWeight(characterRequestDto.getWeight());
        character.setHistory(characterRequestDto.getHistory());
        character.setImage(Utils.setUpImageOrThrowException(file));
        return mapper.characterToCharacterAllResponseDto(repository.save(character));
    }

    @Transactional(readOnly = true)
    @Override
    public CharacterResponseDto getCharactersByName(String name) {
        Character character = repository.findCharactersByName(name);
        if (Objects.nonNull(character)) {
            return mapper.characterToCharacterResponseDto(character);
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CharacterResponseDto> getCharactersByAge(int age) {
        List<Character> characters = repository.findCharactersByAge(age);
        if (Objects.nonNull(characters)) {
            return mapper.charactersToCharacterResponseDtos(characters);
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CharacterResponseDto> getCharactersByMovieId(Long movieId) {
        return mapper.charactersToCharacterResponseDtos(repository.findCharacterByMovieId(movieId));
    }

    @Transactional
    @Override
    public CharacterAllResponseDto addMovieToCharacter(Long movieId, Long characterId) {
        Character character = getCharacter(characterId);
        Movie movie = movieRepository.findById(movieId).orElseThrow(() ->
            new IllegalArgumentException("Movie with id: " + movieId + " could not be found."));
        if (character.getMovies().size() == 0) {
            character.getMovies().add(movie);
            movie.getCharacters().add(character);
            return mapper.characterToCharacterAllResponseDto(repository.save(character));
        } else if (character.getMovies().contains(movie)) {
            throw new IllegalArgumentException("The character already in movie.");
        } else {
            character.getMovies().add(movie);
            movie.getCharacters().add(character);
            return mapper.characterToCharacterAllResponseDto(repository.save(character));
        }
    }

    @Transactional
    @Override
    public CharacterAllResponseDto removeMovieFromCharacter(Long movieId, Long characterId) {
        Character character = getCharacter(characterId);
        Movie movie = movieRepository.findById(movieId).orElseThrow(() ->
            new IllegalArgumentException("Movie with id: " + movieId + " could not be found."));
        if (character.getMovies().size() == 0) {
            throw new IllegalArgumentException("The character does have movies to remove.");
        }
        if (character.getMovies().contains(movie)) {
            movie.getCharacters().remove(character);
            character.getMovies().remove(movie);
            return mapper.characterToCharacterAllResponseDto(repository.save(character));
        } else {
            throw new IllegalArgumentException(
                "Character haven´t  the movie with id : " + movieId + " associated.");
        }
    }

}
