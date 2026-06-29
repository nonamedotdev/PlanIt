package com.planit.service;

import com.planit.model.Conflict;
import com.planit.model.Event;
import com.planit.model.EventComponent;
import com.planit.model.Resource;
import com.planit.model.TimeSlot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Detects overlapping resource usage,
 * (eg. the same venue or staff member booked twice at the same time).
 */
public class ConflictDetectionService {

    private static final Logger LOGGER = Logger.getLogger(ConflictDetectionService.class.getName());

    // All resources known to the system, used when looking for alternatives.
    private List<Resource> allResources = new ArrayList<>();

    public ConflictDetectionService() {
    }

    public ConflictDetectionService(List<Resource> allResources) {
        this.allResources = allResources;
    }

    public void setAllResources(List<Resource> allResources) {
        this.allResources = allResources;
    }

    //Checks all components of the event and reports a conflict
    public List<Conflict> detectConflicts(Event e) {
        List<Conflict> conflicts = new ArrayList<>();
        List<EventComponent> components = e.getComponents();

        for (int i = 0; i < components.size(); i++) {
            for (int j = i + 1; j < components.size(); j++) {
                EventComponent c1 = components.get(i);
                EventComponent c2 = components.get(j);

                for (Resource r1 : c1.getAssignedResources()) {
                    for (Resource r2 : c2.getAssignedResources()) {
                        if (r1.getId().equals(r2.getId()) && timesOverlap(c1, c2)) {
                            LocalDateTime overlapStart = c1.getStart().isAfter(c2.getStart()) ? c1.getStart() : c2.getStart();
                            LocalDateTime overlapEnd = c1.getEnd().isBefore(c2.getEnd()) ? c1.getEnd() : c2.getEnd();
                            conflicts.add(new Conflict(r1, c1, c2, overlapStart, overlapEnd));
                        }
                    }
                }
            }
        }

        LOGGER.info("Detected " + conflicts.size() + " conflict(s) for event " + e.getId());
        return conflicts;
    }

    private boolean timesOverlap(EventComponent c1, EventComponent c2) {
        return c1.getStart().isBefore(c2.getEnd()) && c2.getStart().isBefore(c1.getEnd()); //
    }

    // Checks whether a resource already has a booking overlapping the given range.
    public boolean hasOverlap(Resource r, LocalDateTime start, LocalDateTime end) {
        return !r.isAvailable(start, end);
    }

    /**
     * TBI. Suggests a free slot and resource
    */
}

