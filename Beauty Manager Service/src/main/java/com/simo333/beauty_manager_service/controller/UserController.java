package com.simo333.beauty_manager_service.controller;

import com.simo333.beauty_manager_service.dto.AppUserPatch;
import com.simo333.beauty_manager_service.model.AppUser;
import com.simo333.beauty_manager_service.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceImpl service;

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<Page<AppUser>> getUsersPage(Pageable page) {
        Page<AppUser> users = service.getUsersPage(page);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUser(@PathVariable Long id) {
        AppUser client = service.getUser(id);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<AppUser> saveUser(@RequestBody @Valid AppUser user) {
        AppUser saved = service.save(user);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long id, @RequestBody @Valid AppUser appUser) {
        appUser.setId(id);
        AppUser updated = service.update(appUser);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @PatchMapping
    public ResponseEntity<AppUser> patchUserWithRoleUser(@RequestBody @Valid AppUserPatch patch) {
        AppUser updated = service.patchWithRoleUser(patch);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/{id}")
    public ResponseEntity<AppUser> patchUserWithRoleAdmin(@PathVariable Long id, @RequestBody @Valid AppUserPatch patch) {
        AppUser updated = service.patchWithRoleAdmin(id, patch);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
