package co.com.disney.film.controller;

import co.com.disney.film.domain.dto.mapper;
import co.com.disney.film.domain.dto.requestDto.MovieRequestDto;
import co.com.disney.film.domain.dto.responseDto.MoviePartialResponseDto;
import co.com.disney.film.domain.dto.responseDto.MovieResponseDto;
import co.com.disney.film.domain.model.Movie;
import co.com.disney.film.service.implentation.MovieDAOImpl;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieDAOImpl service;

    public MovieController(MovieDAOImpl service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MoviePartialResponseDto>> getMovies() {
        return new ResponseEntity<>(service.getMovies(), HttpStatus.OK);
    }

    @GetMapping("/get/{movieId}")
    public ResponseEntity<MoviePartialResponseDto> getMovie(@PathVariable final Long movieId) {
        return new ResponseEntity<>(
            mapper.movieToMoviePartialResponseDto(service.getMovie(movieId)), HttpStatus.OK);
    }

    @GetMapping("/download/{movieId}")
    public ResponseEntity<Resource> downloadImageMovie(@PathVariable final Long movieId) {
        Movie movie = service.getMovie(movieId);
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(movie.getImage().getFileType()))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + movie.getImage().getFileName() + "\"")
            .body(new ByteArrayResource(movie.getImage().getData()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<MoviePartialResponseDto> addMovie(
        @RequestParam final MultipartFile file,
        @RequestParam final String movie) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MovieRequestDto movieRequestDto = mapper.readValue(movie, MovieRequestDto.class);
        return new ResponseEntity<>(service.addMovie(file, movieRequestDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit/{movieId}")
    public ResponseEntity<MoviePartialResponseDto> editMovie(
        @PathVariable final Long movieId,
        @RequestParam final MultipartFile file,
        @RequestParam final String movie) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MovieRequestDto movieRequestDto = mapper.readValue(movie,MovieRequestDto.class);
        return new ResponseEntity<>(service.editMovie(movieId,file, movieRequestDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable final Long movieId) {
        service.deleteMovie(movieId);
        return new ResponseEntity<>("Movie was deleted.", HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> getMovieByTitle(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Long idGenre,
        @RequestParam(required = false) String order
    ) {
        return new ResponseEntity<>(service.filterMovies(name, idGenre, order), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/movies/{movieId}/character/{characterId}")
    public ResponseEntity<MoviePartialResponseDto> addCharacterToMovie(
        @PathVariable final Long characterId, @PathVariable final Long movieId) {
        return new ResponseEntity<>(service.addCharacterToMovie(characterId, movieId),
            HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/movies/{movieId}/character/{characterId}")
    public ResponseEntity<MoviePartialResponseDto> deleteMovieFromCharacter(
        @PathVariable final Long characterId, @PathVariable final Long movieId) {
        return new ResponseEntity<>(service.deleteMovieFromCharacter(characterId, movieId),
            HttpStatus.OK);
    }

}
