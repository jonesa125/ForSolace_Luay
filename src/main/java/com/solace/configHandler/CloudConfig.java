package com.solace.configHandler;

import com.solace.configHandler.PropertiesInterface;

import java.util.Map;

public class CloudConfig {
    private String provider;
    private Map<String, Object> properties;

    // Getters and Setters
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }


    public Map<String, Object> getProperties() { // Ensure getter is present
        return properties;
    }

    public void setProperties(Map<String, Object> properties) { // Ensure setter is present
        this.properties = properties;
    }

}