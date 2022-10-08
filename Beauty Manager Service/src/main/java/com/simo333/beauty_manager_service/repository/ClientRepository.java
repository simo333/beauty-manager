package com.simo333.beauty_manager_service.repository;


import com.simo333.beauty_manager_service.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsById(Long id);

    boolean existsByFirstNameAndLastNameAndPhoneNumber(String firstName, String lastName, String phoneNumber);

    Optional<Client> findDistinctByPhoneNumber(String phoneNumber);

    Optional<Client> findByFirstNameAndLastNameAndPhoneNumber(String firstName, String lastName, String phoneNumber);
}
