package co.com.disney.film.security.repository;

import co.com.disney.film.security.domain.enums.RoleList;
import co.com.disney.film.security.domain.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleList roleName);
}
