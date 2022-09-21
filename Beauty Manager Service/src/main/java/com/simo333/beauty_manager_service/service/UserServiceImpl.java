package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.model.AppUser;
import com.simo333.beauty_manager_service.model.Role;
import com.simo333.beauty_manager_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncode;

    @Transactional
    @Override
    public AppUser save(AppUser user) {
        log.info("Saving a new user: {}", user.getEmail());
        user.setPassword(passwordEncode.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<AppUser> getAll() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public AppUser getUser(Long userId) {
        AppUser user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id '{}' not found", userId);
            throw new ResourceNotFoundException("User not found.");
        });
        log.info("User '{}' has been found.", user.getEmail());
        return user;
    }

    @Override
    public AppUser getUser(String email) {
        AppUser user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User with email '{}' not found", email);
            throw new ResourceNotFoundException("User not found.");
        });
        log.info("User '{}' has been found.", user.getEmail());
        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            log.info("User with email '{}' has been found.", email);
            return true;
        }
        log.info("User with email '{}' not found.", email);
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = getUser(email);
        return buildUserDetails(user);
    }

    /*T TODO consider: block editing user's email? */
    @Transactional
    @Override
    public AppUser update(AppUser user) {
        getUser(user.getId());
        log.info("Updating user with id '{}'", user.getId());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void delete(long userId) {
        log.info("Deleting user with id '{}'", userId);
        userRepository.deleteById(userId);
    }

    @Transactional
    @Override
    public void addRoleToUser(Long userId, Long roleId) {
        AppUser user = getUser(userId);
        Role role = roleService.getRole(roleId);
        log.info("Adding role '{}' to user '{}'", role.getName(), user.getEmail());
        user.getRoles().add(role);
    }

    @Transactional
    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        AppUser user = getUser(userId);
        Role role = roleService.getRole(roleId);
        log.info("Removing role '{}' from user '{}'", role.getName(), user.getEmail());
        user.getRoles().remove(role);
    }

    public static User buildUserDetails(AppUser user) {
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());

        return new User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
