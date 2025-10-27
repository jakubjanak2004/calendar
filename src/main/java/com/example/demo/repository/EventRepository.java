package com.example.demo.repository;

import com.example.demo.model.Event;
import com.example.demo.model.EventOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventOwnerAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            EventOwner eventOwner, Instant rangeEndInclusive, Instant rangeStartInclusive);
}
