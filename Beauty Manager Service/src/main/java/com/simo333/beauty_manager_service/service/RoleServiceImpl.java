package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.model.Role;
import com.simo333.beauty_manager_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAll() {
        log.info("Fetching all roles");
        return roleRepository.findAll();
    }

    @Override
    public Role getRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> {
            log.error("Role with id '{}' not found", roleId);
            throw new IllegalArgumentException("Role not found. For id " + roleId);
        });
        log.info("Role '{}' has been found.", role.getName());
        return role;
    }

    @Override
    public Role getRole(String roleName) {
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> {
            log.error("Role with name '{}' not found", roleName);
            throw new IllegalArgumentException("Role not found. For name " + roleName);
        });
        log.info("Role '{}' has been found.", role.getName());
        return role;
    }

    @Transactional
    @Override
    public Role save(Role role) {
        log.info("Saving role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public Role update(Role role) {
        getRole(role.getId());
        log.info("Updating role with id '{}'", role.getId());
        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public void deleteById(long roleId) {
        log.info("Deleting role with id '{}'", roleId);
        roleRepository.deleteById(roleId);
    }


}
