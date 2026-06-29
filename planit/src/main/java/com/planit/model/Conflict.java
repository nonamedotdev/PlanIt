package com.planit.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Conflict object that highlights the conflicts.
 */
public class Conflict implements Serializable {

    private static final long serialVersionUID = 1L;

    private Resource resource;
    private EventComponent comp1;
    private EventComponent comp2;
    private LocalDateTime overlapStart;
    private LocalDateTime overlapEnd;

    public Conflict(Resource resource, EventComponent comp1, EventComponent comp2,
                     LocalDateTime overlapStart, LocalDateTime overlapEnd) {
        this.resource = resource;
        this.comp1 = comp1;
        this.comp2 = comp2;
        this.overlapStart = overlapStart;
        this.overlapEnd = overlapEnd;
    }

    public String getDescription() {
        return "Resource \"" + resource.getName() + "\" is double-booked between \""
                + comp1.getTitle() + "\" and \"" + comp2.getTitle() + "\" from "
                + overlapStart + " to " + overlapEnd;
    }

    public Resource getResource() {
        return resource;
    }

    public EventComponent getComp1() {
        return comp1;
    }

    public EventComponent getComp2() {
        return comp2;
    }

    public LocalDateTime getOverlapStart() {
        return overlapStart;
    }

    public LocalDateTime getOverlapEnd() {
        return overlapEnd;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
