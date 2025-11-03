package com.example.demo.mapper;

import com.example.demo.dto.request.SignUpDTO;
import com.example.demo.model.CalendarUser;
import org.springframework.stereotype.Component;

@Component
public class SignUpMapper implements Mapper<CalendarUser, SignUpDTO>{
    @Override
    public CalendarUser toEntity(SignUpDTO signupDTO) {
        return new CalendarUser(signupDTO.getFirstName(), signupDTO.getLastName(), signupDTO.getUsername(), signupDTO.getPassword());
    }

    @Override
    public SignUpDTO toDTO(CalendarUser calendarUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CalendarUser updateEntity(CalendarUser calendarUser, SignUpDTO signUpDTO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
