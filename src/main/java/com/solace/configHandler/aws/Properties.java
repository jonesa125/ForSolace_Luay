package com.solace.configHandler.aws;

import com.solace.configHandler.PropertiesInterface;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode //annotation is for the test
public class Properties<T> implements PropertiesInterface {

    private String region;
    private Features features;
    private String action;
    // Getters and Setters
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Features getFeatures() {
        return features;
    }

    public void setFeatures(Features features) {
        this.features = features;
    }

    @Override
    public boolean validate() {
        if (region != null || features !=null ){
            return true;
        }
        return false;
    }
}