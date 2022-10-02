package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.dto.AppUserPatch;
import com.simo333.beauty_manager_service.model.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Page<AppUser> getUsersPage(Pageable page);

    AppUser save(AppUser user);

    AppUser getUser(Long userId);

    AppUser getUser(String email);

    boolean existsByEmail(String email);

    AppUser update(AppUser user);

    AppUser patchWithRoleUser(AppUserPatch patch);

    AppUser patchWithRoleAdmin(Long id, AppUserPatch patch);

    void deleteById(long userId);

    void addRoleToUser(Long userId, Long roleId);

    void removeRoleFromUser(Long userId, Long roleId);
}
