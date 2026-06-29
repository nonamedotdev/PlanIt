package com.planit.model;

import java.time.LocalDateTime;

/**
 * Represents a service slot (eg catering).
 */
public class ServiceSlot extends EventComponent {

    private static final long serialVersionUID = 1L;

    private String serviceType;
    private double flatRate;
    private String providerName;

    public ServiceSlot(String id, String title, LocalDateTime start, LocalDateTime end,
                        String serviceType, double flatRate, String providerName) {
        super(id, title, start, end);
        this.serviceType = serviceType;
        this.flatRate = flatRate;
        this.providerName = providerName;
    }

    @Override
    public double calculateCost() {
        return flatRate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public double getFlatRate() {
        return flatRate;
    }

    public void setFlatRate(double flatRate) {
        this.flatRate = flatRate;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}
