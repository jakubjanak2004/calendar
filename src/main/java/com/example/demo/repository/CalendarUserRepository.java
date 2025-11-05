package com.example.demo.repository;

import com.example.demo.model.CalendarUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CalendarUserRepository extends JpaRepository<CalendarUser, UUID> {
    boolean existsByUsername(String username);
    Optional<CalendarUser> findByUsername(String username);
}
