package com.solace.configHandler;

public class IacConfig {
    private String name;
    private String version;
    private CloudConfig cloud;
    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public CloudConfig getCloud() {
        return cloud;
    }

    public void setCloud(CloudConfig cloud) {
        this.cloud = cloud;
    }
}
