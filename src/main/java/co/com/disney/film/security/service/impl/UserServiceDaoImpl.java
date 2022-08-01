package co.com.disney.film.security.service.impl;

import co.com.disney.film.security.domain.model.User;
import co.com.disney.film.security.repository.UserRepository;
import co.com.disney.film.security.service.contract.UserServiceDao;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceDaoImpl implements UserServiceDao {

    private final UserRepository userRepository;

    public UserServiceDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
