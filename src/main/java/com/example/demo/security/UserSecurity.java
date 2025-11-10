package com.example.demo.security;

import com.example.demo.model.CalendarUser;
import com.example.demo.repository.CalendarUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {
    private final CalendarUserRepository calendarUserRepository;

    public boolean isUser(UUID calendarUserId, Authentication auth) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(auth.getName()).orElseThrow();
        return calendarUserId.equals(calendarUser.getId());
    }
}
