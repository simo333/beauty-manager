package com.simo333.beauty_manager_service.controller;

import com.simo333.beauty_manager_service.exception.RefreshTokenException;
import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.model.RefreshToken;
import com.simo333.beauty_manager_service.model.Role;
import com.simo333.beauty_manager_service.model.User;
import com.simo333.beauty_manager_service.security.jwt.JwtUtils;
import com.simo333.beauty_manager_service.security.payload.security.LoginRequest;
import com.simo333.beauty_manager_service.security.payload.security.RegisterRequest;
import com.simo333.beauty_manager_service.security.payload.security.UserInfoResponse;
import com.simo333.beauty_manager_service.service.ClientService;
import com.simo333.beauty_manager_service.service.RefreshTokenService;
import com.simo333.beauty_manager_service.service.RoleService;
import com.simo333.beauty_manager_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final ClientService clientService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user.getUsername());
        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        RefreshToken refreshToken = refreshTokenService.create(user.getUsername());
        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new UserInfoResponse(user.getUsername(), roles));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already taken!");
        }

        Client client = new Client();
        client.setFirstName(registerRequest.getFirstName());
        client.setLastName(registerRequest.getLastName());
        client.setPhoneNumber(registerRequest.getPhoneNumber());
        clientService.save(client);

        User user = new User(null, registerRequest.getEmail(),
                registerRequest.getPassword(),
                Set.of(roleService.getRole(Role.Type.ROLE_USER)),
                client);

        userService.save(user);
        log.info("User with email '{}' registered successfully.", user.getEmail());
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logoutUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!"anonymousUser".equals(principle.toString())) {
            User loggedUser = userService.getUser(((User) principle).getUsername());
            refreshTokenService.deleteByUser(loggedUser);
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body("You've been signed out.");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        String refreshTokenCookie = jwtUtils.getJwtRefreshFromCookies(request);
        if ((refreshTokenCookie != null) && (refreshTokenCookie.length() > 0)) {
            RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenCookie);
            if (refreshTokenService.verifyExpiration(refreshToken)) {
                ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(refreshToken.getUser().getEmail());
                log.info("Token refreshed successfully.");
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                        .body("Token refreshed successfully.");
            }
        }
        log.error("Refresh token is not in database. For: {}", refreshTokenCookie);
        throw new RefreshTokenException(refreshTokenCookie,
                "Refresh token has expired.");
    }
}
