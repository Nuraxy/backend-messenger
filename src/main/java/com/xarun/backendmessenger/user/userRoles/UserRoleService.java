package com.xarun.backendmessenger.user.userRoles;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(
            UserRoleRepository userRoleRepository
    ) {
        this.userRoleRepository = userRoleRepository;
    }

    public List<UserRole> findAll() {
        return this.userRoleRepository.findAll();
    }

    public UserRole updateUserRole(long id, String roleName) {
        UserRole userRole = userRoleRepository.findById(id).orElseThrow();
        userRole.setRoleName(roleName);
        return userRole;
    }
}

