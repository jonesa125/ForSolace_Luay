package com.solace.cloud.aws.resource.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.rds.model.*;
import java.util.Map;
import com.solace.cloud.aws.service.MockAwsService;
import com.solace.cloud.CloudResourceManager;


public class RdsResourceManager implements CloudResourceManager<DBInstance> {
    private static final Logger logger = LoggerFactory.getLogger(RdsResourceManager.class);
    private final MockAwsService mockAws;
    public RdsResourceManager(MockAwsService service) {
        this.mockAws = service;
    }


    public void create(Map<String, String> rdsTypes) {
        CreateDbInstanceRequest request = CreateDbInstanceRequest.builder()
                .dbInstanceIdentifier(rdsTypes.get("identifier"))
                .dbInstanceClass(rdsTypes.get("instanceType")) // Example instance type
                .engine(rdsTypes.get("engine")) // Change this to your preferred database engine
                .masterUsername(rdsTypes.get("username")) // Change to your desired master username
                .masterUserPassword(rdsTypes.get("password")) // Set a strong password
                .allocatedStorage(Integer.parseInt(rdsTypes.get("storage"))) // Minimum storage in GB
                .build();

        CreateDbInstanceResponse response = mockAws.createRdsInstance(request);

        if (response != null) {
            String dbInstanceIdentifier = response.dbInstance().dbInstanceIdentifier();
            String dbInstanceStatus = response.dbInstance().dbInstanceStatus();
            logger.info("Created Rds with ID: " + dbInstanceIdentifier);
            logger.info("Created Rds with status: " + dbInstanceStatus);
        }
    }

    public boolean validate() {
        return true;
    }

}