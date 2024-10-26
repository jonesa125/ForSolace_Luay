package com.solace.cloud.aws.resource;

import com.solace.configHandler.aws.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

public class AwsResourceValidation {
    private static final Logger logger = LoggerFactory.getLogger(AwsResourceValidation.class);
    // Regular expression for validating CIDR notation
    private static final String CIDR_REGEX =
            "^((25[0-5]|(2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9]))\\.){3}(25[0-5]|(2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9]))/([8-9]|[1][0-9]|[1][0-9]|2[0-4])$";

    private static final Pattern CIDR_PATTERN = Pattern.compile(CIDR_REGEX);

    public static Map<String, String> validateCreateVPCConfig(Vpc vpc, String region) {
        //Check if VPC configuration is provided
        if (vpc == null) {
            return null;
        }
        // Validate VPC CIDR block
        if (vpc.getCidrblock() == null || !isValidCIDR(vpc.getCidrblock())) {
            throw new IllegalArgumentException("Invalid or missing VPC CIDR block: " + vpc.getCidrblock());
        }
        // Validate Public Subnet
        Subnet subnet = vpc.getSubnet();
        if (subnet == null) {
            throw new IllegalArgumentException("Invalid or missing subnet");
        }
        if (subnet.getCidrblock() == null || !isValidCIDR(subnet.getCidrblock())) {
            throw new IllegalArgumentException("Invalid or missing Public Subnet CIDR block: " + subnet.getCidrblock());
        }
        if (subnet.getAvzone() == null) {
            throw new IllegalArgumentException("Missing subnet availability zone");
        }
        if (!subnet.getAvzone().startsWith(region)) {
            throw new IllegalArgumentException("Subnet availability zone: " + subnet.getAvzone() + "is not in the same region as the configuration specifies: " + region);
        }
        logger.info("VPC Configuration is valid.");
        return AwsResourceConfigMapper.VpcCreateConfigMapper(vpc, subnet);
    }

    private static boolean isValidCIDR(String cidr) {
        return CIDR_PATTERN.matcher(cidr).matches();
    }

    public static Map<String, String> validateCreateEC2Config(Ec2 ec2) {
        //Check if Ec2 configuration is provided
        if (ec2 == null) {
            return null;
        }

        if (ec2.getInsttype() == null) {
            throw new IllegalArgumentException("ec2 Instance type is missing");
        }

        if (ec2.getAmi() == null) {
            throw new IllegalArgumentException("ec2 ami is missing");
        }
        logger.info("EC2 Configuration is valid.");
        return AwsResourceConfigMapper.Ec2CreateConfigMapper(ec2);
    }

    public static Map<String, String> validateUpdateEC2Config(Ec2 ec2) {
        //Check if Ec2 configuration is provided
        if (ec2 == null) {
            return null;
        }

        if (ec2.getState() == null) {
            throw new IllegalArgumentException("ec2 state is missing");
        }

        if (ec2.getId() == null) {
            throw new IllegalArgumentException("ec2 id is missing");
        }

        logger.info("EC2 Configuration is valid.");
        return AwsResourceConfigMapper.Ec2UpdateConfigMapper(ec2);
    }

    public static Map<String, String> validateCreateRDSConfig(Rds rds) {
        //Check if rds configuration is provided
        if (rds == null) {
            return null;
        }

        if (rds.getInsttype() == null) {
            throw new IllegalArgumentException("rds Instance type is missing");
        }

        if (rds.getIdentifier() == null) {
            throw new IllegalArgumentException("rds identifier is missing");
        }

        if (rds.getStorage() == null) {
            throw new IllegalArgumentException("rds storage is missing");
        }

        if (rds.getEngine() == null) {
            throw new IllegalArgumentException("rds engine is missing");
        }

        if (rds.getUsername() == null) {
            throw new IllegalArgumentException("rds username is missing");
        }

        if (rds.getPassword() == null) {
            throw new IllegalArgumentException("rds password is missing");
        }

        if (rds.getName() == null) {
            throw new IllegalArgumentException("rds name is missing");
        }

        if (rds.getRetention() == null) {
            throw new IllegalArgumentException("rds retention is missing");
        }
        logger.info("RDS Configuration is valid.");
        return AwsResourceConfigMapper.RdsCreateConfigMapper(rds);
    }
}