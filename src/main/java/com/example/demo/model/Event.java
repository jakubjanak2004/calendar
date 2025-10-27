package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    public Event(EventOwner eventOwner, String title, String description, ZoneId timeZone, Instant startTime, Instant endTime) {
        this.eventOwner = eventOwner;
        this.title = title;
        this.description = description;
        this.timeZone = timeZone;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @ManyToOne(optional = false)
    @JoinColumn
    private EventOwner eventOwner;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private ZoneId timeZone;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;
}
