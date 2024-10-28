package com.solace.configHandler.aws;

public class Features {

    private Vpc vpc;
    // Getters and Setters
    public Vpc getVpc() {
        return vpc;
    }
    public void setVpc(Vpc vpc) {
        this.vpc = vpc;
    }

    private Ec2 ec2;
    // Getters and Setters
    public Ec2 getEc2() {
        return ec2;
    }
    public void setEc2(Ec2 ec2) {
        this.ec2 = ec2; }

    private Rds rds;
    // Getters and Setters
    public Rds getRds() {
        return rds;
    }
    public void setRds(Rds rds) {
        this.rds = rds;
    }

}