package co.com.disney.film.security.service.impl;

import co.com.disney.film.security.domain.enums.RoleList;
import co.com.disney.film.security.domain.model.Role;
import co.com.disney.film.security.repository.RoleRepository;
import co.com.disney.film.security.service.contract.RoleServiceDao;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceDaoImpl implements RoleServiceDao {

    private final RoleRepository roleRepository;

    public RoleServiceDaoImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Role> getByRoleName(RoleList roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
