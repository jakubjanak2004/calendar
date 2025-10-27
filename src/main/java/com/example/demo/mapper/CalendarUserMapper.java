package com.example.demo.mapper;

import com.example.demo.dto.response.CalendarUserDTO;
import com.example.demo.model.CalendarUser;
import org.springframework.stereotype.Component;

@Component
public class CalendarUserMapper implements Mapper<CalendarUser, CalendarUserDTO> {
    @Override
    public CalendarUser toEntity(CalendarUserDTO calendarUserDTO) {
        return null;
    }

    @Override
    public CalendarUserDTO toDTO(CalendarUser calendarUser) {
        return new CalendarUserDTO(calendarUser.getUsername(), calendarUser.getFirstName(), calendarUser.getLastName());
    }
}
