package co.com.disney.film;

import co.com.disney.film.domain.model.Genre;

import co.com.disney.film.repository.CharacterRepository;
import co.com.disney.film.repository.GenreRepository;
import co.com.disney.film.repository.MovieRepository;
import java.util.Optional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DisneyApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisneyApplication.class, args);
    }


    //@Bean
    CommandLineRunner runner(
        GenreRepository repository,
        MovieRepository movieRepository,
        CharacterRepository characterRepository

    ) {
        return args -> {
            //Gender gender1 = new Gender(null, "Action", "/images/action.png", null);
            Optional<Genre> gender = repository.findById(2L);

//            Movie movie = new Movie(null, "/image/poo.png", "Poo", LocalDate.now(), Rate.FIVE, null,
//                new HashSet<Gender>(
//                    List.of(gender.get())));
//            Movie movie1 = movieRepository.save(movie);

            /*Optional<Movie> movie = movieRepository.findById(8L);

            Character character = new Character(null,"/image/pan.png","Peter Pan",15, 45.50,"Amazing tales", new HashSet<>(List.of(movie.get())));
            Character character1 = characterRepository.save(character);*/
            /*movie.get().setCharacters(new HashSet<>(List.of(character1)));
            movieRepository.save(movie.get());*/
        };
    }

}
