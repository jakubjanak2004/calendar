package com.example.demo.service;

import com.example.demo.SystemTest;
import com.example.demo.dto.response.CalendarUserDTO;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.service.utils.Generator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class UserServiceTest extends SystemTest {
    private final Generator generator;
    private final CalendarUserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserServiceTest(Generator generator, CalendarUserRepository userRepository, UserService userService) {
        this.generator = generator;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Test
    public void findAllFindsAllPageableUsersPageable() {
        userRepository.saveAll(generator.createUsers("username", "password", 5));
        Page<CalendarUserDTO> calendarUserDTOPage = userService.findAllPageable(PageRequest.of(0, 10));
        Assertions.assertEquals(5, calendarUserDTOPage.getTotalElements());
    }
}
