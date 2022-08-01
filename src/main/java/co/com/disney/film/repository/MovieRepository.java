package co.com.disney.film.repository;

import co.com.disney.film.domain.model.Movie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findMovieByTitle(String title);

    Optional<List<Movie>> findMovieByGenreGenreId(Long genreId);


}
