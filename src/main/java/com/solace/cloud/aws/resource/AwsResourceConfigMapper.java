package com.solace.cloud.aws.resource;

import com.solace.configHandler.aws.Ec2;
import com.solace.configHandler.aws.Rds;
import com.solace.configHandler.aws.Subnet;
import com.solace.configHandler.aws.Vpc;

import java.util.HashMap;
import java.util.Map;

public class AwsResourceConfigMapper {

    public static Map<String, String> VpcCreateConfigMapper(Vpc vpc, Subnet subnet, String secGroup){
        Map<String, String> vpcParamsMap = new HashMap<>();
        vpcParamsMap.put("vpc_cidr", vpc.getCidrblock());
        vpcParamsMap.put("subnet_cidr", subnet.getCidrblock());
        vpcParamsMap.put("az_region", subnet.getAvzone());
        vpcParamsMap.put("security_group", secGroup);
        return vpcParamsMap;
    }

    public static Map<String, String> Ec2CreateConfigMapper(Ec2 ec2) {
        Map<String, String> ec2Details = new HashMap<>();
        ec2Details.put("instanceType", ec2.getInsttype());
        ec2Details.put("ami", ec2.getAmi());
        return ec2Details;
    }

    public static Map<String, String> Ec2UpdateConfigMapper(Ec2 ec2) {
        Map<String, String> ec2Details = new HashMap<>();
        ec2Details.put("state", ec2.getState());
        ec2Details.put("id", ec2.getId());
        return ec2Details;
    }

    public static Map<String, String> RdsCreateConfigMapper(Rds rds) {
        Map<String, String> rdsDetailsMap = new HashMap<>();
        rdsDetailsMap.put("instanceType", rds.getInsttype());
        rdsDetailsMap.put("identifier", rds.getIdentifier());
        rdsDetailsMap.put("storage", rds.getStorage());
        rdsDetailsMap.put("engine", rds.getEngine());
        rdsDetailsMap.put("username", rds.getUsername());
        rdsDetailsMap.put("password", rds.getPassword());
        rdsDetailsMap.put("name", rds.getName());
        rdsDetailsMap.put("retention", rds.getRetention());
        return rdsDetailsMap;
    }

}
