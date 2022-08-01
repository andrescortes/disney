package co.com.disney.film.security.service.contract;

import co.com.disney.film.security.domain.model.User;
import java.util.Optional;

public interface UserServiceDao {

    Optional<User> getByUsername(String username);

    boolean existByUsername(String username);

    User saveUser(User user);
}
