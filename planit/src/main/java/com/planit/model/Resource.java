package com.planit.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private boolean active;

    public Resource(String id, String name) {
        this.id = id;
        this.name = name;
        this.active = true;
    }

    // Checks whether this resource is free for the given time range.
    public abstract boolean isAvailable(LocalDateTime start, LocalDateTime end);

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return name;
    }
}
