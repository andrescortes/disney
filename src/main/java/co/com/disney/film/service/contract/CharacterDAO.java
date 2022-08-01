package co.com.disney.film.service.contract;

import co.com.disney.film.domain.dto.requestDto.CharacterRequestDto;
import co.com.disney.film.domain.dto.responseDto.CharacterAllResponseDto;
import co.com.disney.film.domain.dto.responseDto.CharacterResponseDto;
import co.com.disney.film.domain.model.Character;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CharacterDAO extends GenericDAO<Character> {

    CharacterAllResponseDto addCharacter(MultipartFile file,CharacterRequestDto characterRequestDto)
        throws Exception;

    List<CharacterAllResponseDto> getCharacters();

    CharacterAllResponseDto getCharacterById(Long characterId);


    Character getCharacter(Long characterId);

    void deleteCharacter(Long characterId);

    CharacterAllResponseDto editCharacter(Long characterId,MultipartFile file, CharacterRequestDto characterRequestDto)
        throws Exception;

    CharacterResponseDto getCharactersByName(String name);

    List<CharacterResponseDto> getCharactersByAge(int age);

    List<CharacterResponseDto> getCharactersByMovieId(Long movieId);

    CharacterAllResponseDto addMovieToCharacter(Long movieId, Long characterId);

    CharacterAllResponseDto removeMovieFromCharacter(Long movieId, Long characterId);





}

