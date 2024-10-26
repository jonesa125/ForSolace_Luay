package com.solace.configHandler.aws;

public class Ec2 {
    private String insttype;
    private String ami;
    private String state;
    private String id;

    // Getters and Setters
    public String getInsttype() {
        return insttype;
    }

    public void setInsttype(String insttype) {
        this.insttype = insttype;
    }

    public String getAmi() {
        return ami;
    }

    public void setAmi(String ami) {
        this.ami = ami;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}