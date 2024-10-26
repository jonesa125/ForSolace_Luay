package com.solace.cloud.aws.resource.manager;

import com.solace.cloud.aws.resource.AwsResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.ec2.model.*;
import java.util.List;
import java.util.Map;
import com.solace.cloud.aws.service.MockAwsService;
import com.solace.cloud.CloudResourceManager;

public class Ec2ResourceManager implements CloudResourceManager<Instance> {
    private static final Logger logger = LoggerFactory.getLogger(Ec2ResourceManager.class);
    private final MockAwsService mockAws;
    public Ec2ResourceManager(MockAwsService service) {
        this.mockAws = service;
    }


    public void create(Map<String,String> ec2Map, String subnetId) {
        // Create a request to launch an EC2 instance
        RunInstancesRequest runRequest = RunInstancesRequest.builder()
                .imageId(ec2Map.get("ami")) // Replace with a valid AMI ID
                .instanceType(ec2Map.get("instanceType"))
                .subnetId(subnetId)
                .minCount(1)
                .maxCount(1)
                .build();

        // Launch the instance
        RunInstancesResponse response = mockAws.runInstances(runRequest);
        List<Instance> instances = response.instances();
        if (instances != null && !instances.isEmpty()) {
            String instanceId = instances.get(0).instanceId();
            String instanceType = instances.get(0).instanceTypeAsString();
            logger.info("Successfully launched EC2 instance: " + instanceId + " of type: " + instanceType);

        } else {
            logger.error("Failed to launch EC2 instance.");
        }
    }

    public void update(String ec2Id, String state) {
        if (state.equals("stop")) {
            StopInstancesRequest stopRequest = StopInstancesRequest.builder()
                    .instanceIds(ec2Id)
                    .build();

            mockAws.stopInstances(stopRequest);
            logger.info("Stopped Ec2 instance: " + ec2Id);
        }
    }

    public boolean validate() {
        return true;
    }
}