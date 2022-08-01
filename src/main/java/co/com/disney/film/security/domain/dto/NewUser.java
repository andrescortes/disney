package co.com.disney.film.security.domain.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewUser {

    private String username;
    private String email;
    private String password;
    private Set<String> roles = new HashSet<>();
}
