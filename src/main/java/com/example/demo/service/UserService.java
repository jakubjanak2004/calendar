package com.example.demo.service;

import com.example.demo.mapper.CalendarUserMapper;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.demo.dto.response.CalendarUserDTO;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<CalendarUserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(CalendarUserMapper::toDTO);
    }
}
