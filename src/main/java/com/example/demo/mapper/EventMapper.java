package com.example.demo.mapper;

import com.example.demo.dto.response.EventDTO;
import com.example.demo.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper implements Mapper<Event, EventDTO>{
    @Override
    public Event toEntity(EventDTO eventDTO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EventDTO toDTO(Event event) {
        return new EventDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getStartTime(),
                event.getEndTime()
        );
    }

    @Override
    public Event updateEntity(Event event, EventDTO eventDTO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
