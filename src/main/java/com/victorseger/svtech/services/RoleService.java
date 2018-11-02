package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.Role;
import com.victorseger.svtech.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role getOne(Long id) {
        if (id != null && roleRepository.existsById(id))
            return roleRepository.getOne(id);
        return null;
    }

    public boolean existsById(Long id) {
        if (id != null)
            return roleRepository.existsById(id);
        return false;
    }

    public Role findByRole(String role) {
        if (role != null) {
            return roleRepository.findByRole(role);
        }
        return null;
    }
}
