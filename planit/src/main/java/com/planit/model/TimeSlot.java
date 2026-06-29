package com.planit.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Duration;

/**
 * Defines a temporal scheduling interval, used to track bookings
 * of Venues and shifts of Staff.
 * A seperate object to be used more flexibly.
 */
public class TimeSlot implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime start;
    private LocalDateTime end;
    private String componentId;

    public TimeSlot(LocalDateTime start, LocalDateTime end, String componentId) {
        this.start = start;
        this.end = end;
        this.componentId = componentId;
    }

    // Checks whether this slot overlaps with another slot
    public boolean overlaps(TimeSlot ts) {
        return start.isBefore(ts.end) && ts.start.isBefore(end);
    }

    public double getDurationHours() {
        return Duration.between(start, end).toMinutes() / 60.0;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }
}
