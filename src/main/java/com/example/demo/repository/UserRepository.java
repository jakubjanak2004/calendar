package com.example.demo.repository;

import com.example.demo.model.CalendarUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CalendarUser, Long> {
    boolean existsByUsername(String username);
    Optional<CalendarUser> findByUsername(String username);
}
