package com.simo333.beauty_manager_service.service.impl;

import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.model.RefreshToken;
import com.simo333.beauty_manager_service.model.Role;
import com.simo333.beauty_manager_service.model.User;
import com.simo333.beauty_manager_service.repository.ClientRepository;
import com.simo333.beauty_manager_service.repository.RefreshTokenRepository;
import com.simo333.beauty_manager_service.repository.RoleRepository;
import com.simo333.beauty_manager_service.repository.UserRepository;
import com.simo333.beauty_manager_service.service.RefreshTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class RefreshTokenServiceImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RefreshTokenService tokenService;
    @Autowired
    private RefreshTokenRepository tokenRepository;

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        clientRepository.deleteAll();
        roleRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    void shouldReturnToken() {
        // given
        String tokenName = "token";
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().plus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS));
        token.setToken(tokenName);

        // when
        tokenRepository.save(token);
        RefreshToken actualToken = tokenService.findByToken(tokenName);

        // then
        assertThat(actualToken).isEqualTo(token);
    }

    @Test
    void shouldReturnExpirationValid() {
        // given
        String tokenName = "token";
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().plus(1, ChronoUnit.HOURS));
        token.setToken(tokenName);

        // when
        tokenRepository.save(token);
        boolean validation = tokenService.verifyExpiration(token);

        // then
        assertThat(validation).isTrue();
    }

    @Test
    void shouldReturnTokenExpired() {
        // given
        String tokenName = "token";
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().minus(1, ChronoUnit.HOURS));
        token.setToken(tokenName);

        // when
        tokenRepository.save(token);
        boolean validation = tokenService.verifyExpiration(token);

        // then
        assertThat(validation).isFalse();
    }

    @Test
    void shouldDeleteByUser() {
        // given
        User user = new User();
        Role roleUser = new Role(null, Role.Type.ROLE_USER);
        Client client = new Client(null, "name", "lastName", "+11123456789");
        user.setRoles(Set.of(roleUser));
        user.setPassword("password");
        user.setEmail("mail@mail.com");
        user.setClient(client);

        String tokenName = "token";
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().plus(1, ChronoUnit.HOURS));
        token.setToken(tokenName);
        token.setUser(user);

        // when
        roleRepository.save(roleUser);
        clientRepository.save(client);
        userRepository.save(user);
        tokenRepository.save(token);
        tokenService.deleteByUser(user);

        // then
        assertThat(tokenRepository.findById(token.getId())).isEmpty();
    }
}