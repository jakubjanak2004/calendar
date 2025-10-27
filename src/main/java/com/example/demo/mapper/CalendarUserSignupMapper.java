package com.example.demo.mapper;

import com.example.demo.dto.request.SignUpDTO;
import com.example.demo.model.CalendarUser;
import org.springframework.stereotype.Component;

@Component
public class CalendarUserSignupMapper implements Mapper<CalendarUser, SignUpDTO>{
    @Override
    public CalendarUser toEntity(SignUpDTO signupDTO) {
        return new CalendarUser(signupDTO.getFirstName(), signupDTO.getLastName(), signupDTO.getUsername(), signupDTO.getPassword());
    }

    @Override
    public SignUpDTO toDTO(CalendarUser calendarUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
