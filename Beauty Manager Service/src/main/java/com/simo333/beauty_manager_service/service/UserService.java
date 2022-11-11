package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.security.payload.user.AppUserPatch;
import com.simo333.beauty_manager_service.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Page<User> getUsersPage(Pageable page);

    User save(User user);

    User getUser(Long userId);

    User getUser(String email);

    boolean existsByEmail(String email);

    User update(User user);

    User patchWithRoleUser(AppUserPatch patch);

    User patchWithRoleAdmin(Long id, AppUserPatch patch);

    void deleteById(long userId);

    void addRoleToUser(Long userId, Long roleId);

    void removeRoleFromUser(Long userId, Long roleId);
}
