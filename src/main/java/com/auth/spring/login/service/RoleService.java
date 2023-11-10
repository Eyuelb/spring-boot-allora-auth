package com.auth.spring.login.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.spring.login.models.Role;
import com.auth.spring.login.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Integer roleId) {
        return roleRepository.findById(roleId);
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role updateRole(Integer roleId, Role updatedRole) {
        if (roleRepository.existsById(roleId)) {
            updatedRole.setId(roleId);
            return roleRepository.save(updatedRole);
        }
        return null;
    }

    public void deleteRole(Integer roleId) {
        roleRepository.deleteById(roleId);
    }
}
