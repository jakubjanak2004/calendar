package com.example.demo.mapper;

import com.example.demo.dto.request.SignUpDTO;
import com.example.demo.model.CalendarUser;

public class CalendarUserMapper {

    public static CalendarUser toEntity(SignUpDTO signupDTO) {
        return new CalendarUser(
                signupDTO.getFirstName(),
                signupDTO.getLastName(),
                signupDTO.getUsername(),
                signupDTO.getPassword()
        );
    }
}
