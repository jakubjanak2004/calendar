package com.example.demo.mapper;

import com.example.demo.dto.request.UpdateUserDTO;
import com.example.demo.model.CalendarUser;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserMapper implements Mapper<CalendarUser, UpdateUserDTO>{
    @Override
    public CalendarUser toEntity(UpdateUserDTO updateUserDTO) {
        return null;
    }

    @Override
    public UpdateUserDTO toDTO(CalendarUser calendarUser) {
        return null;
    }

    @Override
    public CalendarUser updateEntity(CalendarUser calendarUser, UpdateUserDTO updateUserDTO) {
        calendarUser.setFirstName(updateUserDTO.getFirstName());
        calendarUser.setLastName(updateUserDTO.getLastName());
        calendarUser.setColor(updateUserDTO.getColor());
        return calendarUser;
    }
}
