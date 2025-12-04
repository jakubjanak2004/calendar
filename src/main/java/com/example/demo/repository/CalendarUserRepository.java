package com.example.demo.repository;

import com.example.demo.model.CalendarUser;
import com.example.demo.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CalendarUserRepository extends JpaRepository<CalendarUser, UUID> {
    boolean existsByUsername(String username);
    Optional<CalendarUser> findByUsername(String username);
}
