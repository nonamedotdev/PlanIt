package com.planit.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * The small building blocks of each event.
 */
public abstract class EventComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<Resource> assignedResources = new ArrayList<>();

    public EventComponent(String id, String title, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public void assignResource(Resource r) {
        assignedResources.add(r);
    }

    public void removeResource(String id) {
        assignedResources.removeIf(r -> r.getId().equals(id));
    }

    public List<Resource> getAssignedResources() {
        return assignedResources;
    }

    // Each component type calculates its own cost.
    public abstract double calculateCost();

    public long getDuration() {
        return Duration.between(start, end).toMinutes();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public String toString() {
        return title;
    }
}
