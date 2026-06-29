package com.planit.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * Represents a physical event location.
 */
public class Venue extends Resource {

    private static final long serialVersionUID = 1L;

    private String location;
    private int capacity;
    private List<String> facilities = new ArrayList<>();
    private List<TimeSlot> bookedSlots = new ArrayList<>();

    public Venue(String id, String name, String location, int capacity) {
        super(id, name);
        this.location = location;
        this.capacity = capacity;
    }

    public boolean isAvailable(LocalDateTime start, LocalDateTime end) {
        TimeSlot requested = new TimeSlot(start, end, null);
        for (TimeSlot ts : bookedSlots) {
            if (ts.overlaps(requested)) {
                return false;
            }
        }
        return true;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void addBooking(TimeSlot ts) {
        bookedSlots.add(ts);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public List<TimeSlot> getBookedSlots() {
        return bookedSlots;
    }
}
