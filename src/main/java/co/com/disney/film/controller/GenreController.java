package co.com.disney.film.controller;

import co.com.disney.film.domain.dto.mapper;
import co.com.disney.film.domain.dto.requestDto.GenreRequestDto;
import co.com.disney.film.domain.dto.responseDto.GenreAllResponseDto;
import co.com.disney.film.domain.dto.responseDto.GenreResponseDto;
import co.com.disney.film.domain.model.Genre;
import co.com.disney.film.service.implentation.GenreDAOImpl;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreDAOImpl service;

    public GenreController(GenreDAOImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<GenreAllResponseDto>> getGenres() {
        return new ResponseEntity<>(service.getGenres(), HttpStatus.OK);
    }

    @GetMapping("/download/{genreId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable final Long genreId) {
        Genre genre = service.getGenre(genreId);
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(genre.getImage().getFileType()))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + genre.getImage().getFileName() + "\"")
            .body(new ByteArrayResource(genre.getImage().getData()));
    }

    @GetMapping("/get/{genreId}")
    public ResponseEntity<GenreAllResponseDto> getGenre(@PathVariable final Long genreId) {
        return new ResponseEntity<>(mapper.genreToGenreAllResponseDto(service.getGenre(genreId)),
            HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GenreAllResponseDto> addGenre(
        @RequestParam MultipartFile file,
        @RequestParam String genre) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        GenreRequestDto genreRequestDto = mapper.readValue(genre, GenreRequestDto.class);
        return new ResponseEntity<>(service.addGenre(file, genreRequestDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{genreId}")
    public ResponseEntity<String> deleteGenre(@PathVariable final Long genreId) {
        service.deleteGenre(genreId);
        return new ResponseEntity<>("Genre was deleted.", HttpStatus.OK);
    }

}
