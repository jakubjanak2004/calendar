package com.example.demo.mapper;

import com.example.demo.dto.response.CalendarUserDTO;
import com.example.demo.model.CalendarUser;
import org.springframework.stereotype.Component;

@Component
public class CalendarUserMapper implements Mapper<CalendarUser, CalendarUserDTO> {
    @Override
    public CalendarUser toEntity(CalendarUserDTO calendarUserDTO) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public CalendarUserDTO toDTO(CalendarUser calendarUser) {
        return new CalendarUserDTO(calendarUser.getId(), calendarUser.getUsername(), calendarUser.getFirstName(), calendarUser.getLastName());
    }

    @Override
    public CalendarUser updateEntity(CalendarUser calendarUser, CalendarUserDTO calendarUserDTO) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
