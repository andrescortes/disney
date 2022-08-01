package co.com.disney.film.security.service.impl;


import co.com.disney.film.security.domain.model.MainUser;
import co.com.disney.film.security.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceDaoImpl userServiceDaoImpl;

    public UserDetailsServiceImpl(UserServiceDaoImpl userServiceDaoImpl) {
        this.userServiceDaoImpl = userServiceDaoImpl;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userServiceDaoImpl.getByUsername(username).orElseThrow(() ->
            new IllegalArgumentException("User with name: " + username + "could not be found."));
        return MainUser.build(user);
    }
}
