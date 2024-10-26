package com.solace.configHandler.aws;

public class Vpc {
    private String cidrblock;
    private Subnet subnet;

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
}
