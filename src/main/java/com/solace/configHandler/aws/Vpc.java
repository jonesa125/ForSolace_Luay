package com.solace.configHandler.aws;

public class Vpc {
    private String cidrblock;
    private Subnet subnet;
    private String securitygroup;
    private String state;

    // Getters and Setters
    public String getCidrblock() {
        return cidrblock;
    }

    public void setCidrblock(String cidrblock) {
        this.cidrblock = cidrblock;
    }

    public Subnet getSubnet() {
        return subnet;
    }

    public void setSubnet(Subnet subnet) {
        this.subnet = subnet;
    }

    public String getSecuritygroup() {
        return securitygroup;
    }

    public void setSecuritygroup(String securitygroup) {
        this.securitygroup = securitygroup;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
