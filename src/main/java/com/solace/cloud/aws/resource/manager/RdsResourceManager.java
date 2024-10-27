package com.solace.cloud.aws.resource.manager;

import com.solace.cloud.aws.AwsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.rds.model.*;

import java.util.List;
import java.util.Map;
import com.solace.cloud.aws.service.MockAwsService;
import com.solace.cloud.CloudResourceManager;


public class RdsResourceManager implements CloudResourceManager {
    private static final Logger logger = LoggerFactory.getLogger(RdsResourceManager.class);
    private final MockAwsService mockAws;
    public RdsResourceManager(MockAwsService service) {
        this.mockAws = service;
    }


    public void create(Map<String, String> rdsTypes, String subnetGroupName, String groupId) {
        String dbInstanceIdentifier = rdsTypes.get("identifier");

        // Step 1: Check if the DB instance identifier is available
        if (!isDbInstanceIdentifierAvailable(dbInstanceIdentifier)) {
            throw new RuntimeException("DB instance identifier " + dbInstanceIdentifier + " is already in use.");
        }

        // Step 2: Create RDS Instance
        CreateDbInstanceRequest request = CreateDbInstanceRequest.builder()
                .dbInstanceIdentifier(dbInstanceIdentifier)
                .dbInstanceClass(rdsTypes.get("instanceType")) // Example instance type
                .engine(rdsTypes.get("engine")) // Change this to your preferred database engine
                .masterUsername(rdsTypes.get("username")) // Change to your desired master username
                .masterUserPassword(rdsTypes.get("password")) // Set a strong password
                .allocatedStorage(Integer.parseInt(rdsTypes.get("storage")))  // Minimum storage in GB
                .vpcSecurityGroupIds(List.of(groupId)) // Specify security group
                .dbSubnetGroupName(subnetGroupName)
                .build();

        CreateDbInstanceResponse response = mockAws.createRdsInstance(request);

        // Step 3: Check if the RDS instance is created successfully
        if (response != null) {
            logger.info("Requested creation of RDS instance with ID: " + dbInstanceIdentifier);
            confirmRdsCreation(dbInstanceIdentifier);
        }
    }

    // Method to check if the DB instance identifier is available
    private boolean isDbInstanceIdentifierAvailable(String dbInstanceIdentifier) {
        DescribeDbInstancesRequest describeRequest = DescribeDbInstancesRequest.builder()
                .dbInstanceIdentifier(dbInstanceIdentifier)
                .build();

        DescribeDbInstancesResponse describeResponse = mockAws.describeRdsInstances(describeRequest);
        // Check if the CIDR block is used in any existing VPCs
        for (DBInstance rdsInstance : describeResponse.dbInstances()) {

            if (rdsInstance.dbInstanceIdentifier().equals(dbInstanceIdentifier)) {
                return false; // DB Instance is already in use
            }
        }
        return true; //DB Instance is available
    }

    // Method to confirm the RDS instance creation
    private void confirmRdsCreation(String dbInstanceIdentifier) {
        DescribeDbInstancesRequest request = DescribeDbInstancesRequest.builder()
                .dbInstanceIdentifier(dbInstanceIdentifier)
                .build();

        DescribeDbInstancesResponse response = mockAws.describeRdsInstances(request);

        if (!response.dbInstances().isEmpty()) {
            String dbInstanceStatus = response.dbInstances().get(0).dbInstanceStatus();
            logger.info("RDS instance is successfully created and in status: " + dbInstanceStatus);
        } else {
            logger.warn("RDS instance not found after creation!");
        }
    }

    public void update(String dbId, String state) {
        if (state.equals(AwsConstants.STOP)) {
            StopDbInstanceRequest stopRequest = StopDbInstanceRequest.builder()
                    .dbInstanceIdentifier(dbId)
                    .build();

            mockAws.stopRdsInstance(stopRequest);
            logger.info("Stopped RDS instance: " + dbId);
        }
    }

    public boolean validate() {
        return true;
    }

}