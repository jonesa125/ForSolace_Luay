package com.solace.configHandler.aws;

public class Subnet {
    private String cidrblock;
    private String avzone;

    // Getters and Setters
    public String getCidrblock() {
        return cidrblock;
    }

    public void setCidrblock(String cidrblock) {
        this.cidrblock = cidrblock;
    }

    public String getAvzone() {
        return avzone;
    }

    public void setAvzone(String avzone) {
        this.avzone = avzone;
    }
}