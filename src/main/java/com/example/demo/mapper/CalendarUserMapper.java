package com.example.demo.mapper;

import com.example.demo.dto.request.SignUpDTO;
import com.example.demo.dto.response.CalendarUserDTO;
import com.example.demo.model.CalendarUser;

public class CalendarUserMapper {

    public static CalendarUser toEntity(SignUpDTO signupDTO) {
        return new CalendarUser(signupDTO.getFirstName(), signupDTO.getLastName(), signupDTO.getUsername(), signupDTO.getPassword());
    }

    public static CalendarUserDTO toDTO(CalendarUser calendarUser) {
        return new CalendarUserDTO(calendarUser.getUsername(), calendarUser.getFirstName(), calendarUser.getLastName());
    }
}
