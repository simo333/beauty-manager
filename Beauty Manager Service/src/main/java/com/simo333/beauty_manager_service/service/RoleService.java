package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAll();

    Role getRole(Role.Type name);

    Role getRole(Long roleId);

    Role save(Role role);

    Role update(Role role);

    void deleteById(long roleId);

}
