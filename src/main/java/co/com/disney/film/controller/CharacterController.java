package co.com.disney.film.controller;

import co.com.disney.film.domain.dto.requestDto.CharacterRequestDto;
import co.com.disney.film.domain.dto.responseDto.CharacterAllResponseDto;
import co.com.disney.film.domain.dto.responseDto.CharacterResponseDto;
import co.com.disney.film.domain.model.Character;
import co.com.disney.film.service.implentation.CharacterDAOImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterDAOImpl service;

    public CharacterController(CharacterDAOImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CharacterAllResponseDto>> getCharacters() {
        return new ResponseEntity<>(service.getCharacters(), HttpStatus.OK);
    }

    @GetMapping("/get/{characterId}")
    public ResponseEntity<CharacterAllResponseDto> getCharacter(
        @PathVariable final Long characterId) {
        return new ResponseEntity<>(service.getCharacterById(characterId), HttpStatus.OK);
    }

    @GetMapping("/download/{characterId}")
    public ResponseEntity<Resource> downloadImage(
        @PathVariable final Long characterId) {
        Character character = service.getCharacter(characterId);
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(character.getImage().getFileType()))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + character.getImage().getFileName() + "\"")
            .body(new ByteArrayResource(character.getImage().getData()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<CharacterAllResponseDto> addCharacter(
        @RequestParam MultipartFile file,
        @RequestParam final String character) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CharacterRequestDto characterRequestDto = mapper.readValue(character,
            CharacterRequestDto.class);
        return new ResponseEntity<>(service.addCharacter(file, characterRequestDto),
            HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit/{characterId}")
    public ResponseEntity<CharacterAllResponseDto> editCharacter(
        @PathVariable final Long characterId,
        @RequestParam MultipartFile file,
        @RequestParam final String character) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CharacterRequestDto characterRequestDto = mapper.readValue(character,
            CharacterRequestDto.class);
        return new ResponseEntity<>(service.editCharacter(characterId, file, characterRequestDto),
            HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{characterId}")
    public ResponseEntity<String> deleteCharacter(
        @PathVariable final Long characterId) {
        service.deleteCharacter(characterId);
        return new ResponseEntity<>("Character was deleted.", HttpStatus.OK);
    }

    @GetMapping("/name")
    public ResponseEntity<?> getCharacterByName(
        @RequestParam(name = "name") final String name) {
        if (service.getCharactersByName(name) != null) {
            return new ResponseEntity<>(service.getCharactersByName(name), HttpStatus.OK);
        }
        return new ResponseEntity<>("Character with name: " + name + "could not be found.",
            HttpStatus.NOT_FOUND);
    }

    @GetMapping("/age")
    public ResponseEntity<List<CharacterResponseDto>> getCharacterByAge(
        @RequestParam(name = "age") final int age) {
        return new ResponseEntity<>(service.getCharactersByAge(age), HttpStatus.OK);
    }

    @GetMapping("movies")
    public ResponseEntity<List<CharacterResponseDto>> getCharacterByMovieId(
        @RequestParam(name = "movies") final Long movies) {
        return new ResponseEntity<>(service.getCharactersByMovieId(movies), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/addMovie/{movieId}/to/character/{characterId}")
    public ResponseEntity<CharacterAllResponseDto> addMovieToCharacter(
        @PathVariable final Long movieId,
        @PathVariable final Long characterId) {
        return new ResponseEntity<>(service.addMovieToCharacter(movieId, characterId),
            HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/removeMovie/{movieId}/from/character/{characterId}")
    public ResponseEntity<CharacterAllResponseDto> removeMovieToCharacter(
        @PathVariable final Long movieId,
        @PathVariable final Long characterId) {
        return new ResponseEntity<>(service.removeMovieFromCharacter(movieId, characterId),
            HttpStatus.OK);
    }

}
