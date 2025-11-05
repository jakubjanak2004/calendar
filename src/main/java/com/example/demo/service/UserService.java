package com.example.demo.service;

import com.example.demo.mapper.CalendarUserMapper;
import com.example.demo.repository.CalendarUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.demo.dto.response.CalendarUserDTO;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CalendarUserRepository calendarUserRepository;
    private final CalendarUserMapper calendarUserMapper;

    public Page<CalendarUserDTO> findAll(Pageable pageable) {
        return calendarUserRepository.findAll(pageable).map(calendarUserMapper::toDTO);
    }
}
