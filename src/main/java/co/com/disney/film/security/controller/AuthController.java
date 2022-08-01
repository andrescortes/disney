package co.com.disney.film.security.controller;

import co.com.disney.film.email.EmailSenderService;
import co.com.disney.film.security.domain.dto.JwtDto;
import co.com.disney.film.security.domain.dto.LoginUser;
import co.com.disney.film.security.domain.dto.NewUser;
import co.com.disney.film.security.domain.enums.RoleList;
import co.com.disney.film.security.domain.model.Role;
import co.com.disney.film.security.domain.model.User;
import co.com.disney.film.security.jwt.JwtProvider;
import co.com.disney.film.security.service.impl.RoleServiceDaoImpl;
import co.com.disney.film.security.service.impl.UserServiceDaoImpl;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceDaoImpl userServiceDaoImpl;
    private final RoleServiceDaoImpl roleServiceDaoImpl;
    private final JwtProvider jwtProvider;

    private final EmailSenderService emailSenderService;


    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
        PasswordEncoder passwordEncoder, UserServiceDaoImpl userServiceDaoImpl,
        RoleServiceDaoImpl roleServiceDaoImpl, JwtProvider jwtProvider,
        EmailSenderService emailSenderService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
        this.userServiceDaoImpl = userServiceDaoImpl;
        this.roleServiceDaoImpl = roleServiceDaoImpl;
        this.jwtProvider = jwtProvider;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody final LoginUser loginUser,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("Credentials invalid, verify fields.",
                HttpStatus.BAD_REQUEST);
        }
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(),
                    loginUser.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generateToken(authentication);
            JwtDto jwtDto = new JwtDto(jwt);
            return new ResponseEntity<>(jwtDto, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error = " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Credentials invalid.",
                HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody final NewUser newUser,
        BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("Checks fields and try again.",
                HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleServiceDaoImpl.getByRoleName(RoleList.ROLE_USER).get());
        if (newUser.getRoles().contains("admin")) {
            roles.add(roleServiceDaoImpl.getByRoleName(RoleList.ROLE_ADMIN).get());
        }
        user.setRoles(roles);
        String body = "Welcome to disney APP";
        String message = emailSenderService.sendSimpleEmail(newUser.getEmail(), body,
            "Register Disney APP");
        userServiceDaoImpl.saveUser(user);
        return new ResponseEntity<>("User saved successfully! Log in. \n" + message, HttpStatus.OK);
    }
}
