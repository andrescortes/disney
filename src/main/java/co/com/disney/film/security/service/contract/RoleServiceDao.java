package co.com.disney.film.security.service.contract;

import co.com.disney.film.security.domain.enums.RoleList;
import co.com.disney.film.security.domain.model.Role;
import java.util.Optional;

public interface RoleServiceDao {

    Optional<Role> getByRoleName(RoleList roleName);
}
