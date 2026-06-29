package com.planit.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents human resources assigned to event components.
 */
public class Staff extends Resource {

    private static final long serialVersionUID = 1L;

    private String role;
    private double hourlyRate;
    private String email;
    private List<TimeSlot> schedule = new ArrayList<>();

    public Staff(String id, String name, String role, double hourlyRate, String email) {
        super(id, name);
        this.role = role;
        this.hourlyRate = hourlyRate;
        this.email = email;
    }

    @Override
    public boolean isAvailable(LocalDateTime start, LocalDateTime end) {
        TimeSlot requested = new TimeSlot(start, end, null);
        for (TimeSlot ts : schedule) {
            if (ts.overlaps(requested)) {
                return false;
            }
        }
        return true;
    }

    public void addShift(TimeSlot ts) {
        schedule.add(ts);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TimeSlot> getSchedule() {
        return schedule;
    }
}
