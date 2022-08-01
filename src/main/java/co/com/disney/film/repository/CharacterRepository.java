package co.com.disney.film.repository;

import co.com.disney.film.domain.model.Character;
import co.com.disney.film.domain.model.Movie;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CharacterRepository extends JpaRepository<Character, Long> {


    Character findCharactersByName(String name);

    List<Character> findCharactersByAge(int age);

    @Query("select c from Character c left join fetch c.movies m where m.movieId=?1")
    List<Character> findCharacterByMovieId(Long movieId);

}
