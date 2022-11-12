package com.simo333.beauty_manager_service.controller;

import com.simo333.beauty_manager_service.security.payload.user.UserPatch;
import com.simo333.beauty_manager_service.model.User;
import com.simo333.beauty_manager_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService service;

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<Page<User>> getUsersPage(Pageable page) {
        Page<User> users = service.getUsersPage(page);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = service.getUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = service.getUser(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @PatchMapping
    public ResponseEntity<User> patchUserWithRoleUser(@RequestBody @Valid UserPatch patch) {
        User updated = service.patchWithRoleUser(patch);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUserWithRoleAdmin(@PathVariable Long id, @RequestBody @Valid UserPatch patch) {
        User updated = service.patchWithRoleAdmin(id, patch);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
