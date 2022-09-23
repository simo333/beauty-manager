package com.simo333.beauty_manager_service.controller;

import com.simo333.beauty_manager_service.exception.RefreshTokenException;
import com.simo333.beauty_manager_service.model.AppUser;
import com.simo333.beauty_manager_service.model.RefreshToken;
import com.simo333.beauty_manager_service.model.Role;
import com.simo333.beauty_manager_service.security.jwt.JwtUtils;
import com.simo333.beauty_manager_service.security.payload.request.LoginRequest;
import com.simo333.beauty_manager_service.security.payload.request.SignUpRequest;
import com.simo333.beauty_manager_service.security.payload.response.MessageResponse;
import com.simo333.beauty_manager_service.security.payload.response.UserInfoResponse;
import com.simo333.beauty_manager_service.service.RefreshTokenService;
import com.simo333.beauty_manager_service.service.RoleServiceImpl;
import com.simo333.beauty_manager_service.service.UserServiceImpl;
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
import org.springframework.security.core.userdetails.User;
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
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }
        // Create new user's account
        AppUser user = new AppUser(null, signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                Set.of(roleService.getRole(Role.Type.ROLE_USER)));

        userService.save(user);
        log.info("User with email '{}' registered successfully.", user.getEmail());
        return ResponseEntity.ok(new MessageResponse("User registered successfully."));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!"anonymousUser".equals(principle.toString())) {
            AppUser loggedUser = userService.getUser(((User) principle).getUsername());
            refreshTokenService.deleteByUser(loggedUser);
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageResponse("You've been signed out."));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshTokenCookie = jwtUtils.getJwtRefreshFromCookies(request);
        if ((refreshTokenCookie != null) && (refreshTokenCookie.length() > 0)) {
            RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenCookie);
            if (refreshTokenService.verifyExpiration(refreshToken)) {
                ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(refreshToken.getUser().getEmail());
                log.info("Token refreshed successfully.");
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                        .header(HttpHeaders.SET_COOKIE, refreshTokenCookie)
                        .body(new MessageResponse("Token refreshed successfully."));
            }
        }
        log.error("Refresh token is not in database. For: {}", refreshTokenCookie);
        throw new RefreshTokenException(refreshTokenCookie,
                "Refresh token has expired.");
    }
}
