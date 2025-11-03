package com.example.demo.mapper;

import com.example.demo.dto.request.EventRequestDTO;
import com.example.demo.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventRequestMapper implements Mapper<Event, EventRequestDTO>{
    @Override
    public Event toEntity(EventRequestDTO eventRequestDTO) {
        return new Event(
          eventRequestDTO.getTitle(),
          eventRequestDTO.getDescription(),
          eventRequestDTO.getStartTime(),
          eventRequestDTO.getEndTime()
        );
    }

    @Override
    public EventRequestDTO toDTO(Event event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Event updateEntity(Event event, EventRequestDTO eventRequestDTO) {
        event.setTitle(eventRequestDTO.getTitle());
        event.setDescription(eventRequestDTO.getDescription());
        event.setStartTime(eventRequestDTO.getStartTime());
        event.setEndTime(eventRequestDTO.getEndTime());
        return event;
    }
}
