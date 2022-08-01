package co.com.disney.film.repository;

import co.com.disney.film.domain.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GenreRepository extends JpaRepository<Genre, Long> {

}
