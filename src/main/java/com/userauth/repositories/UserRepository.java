package com.userauth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userauth.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
}

