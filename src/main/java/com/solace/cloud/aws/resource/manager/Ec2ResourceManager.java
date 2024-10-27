package com.solace.cloud.aws.resource.manager;

import com.solace.cloud.aws.AwsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.solace.cloud.aws.service.MockAwsService;
import com.solace.cloud.CloudResourceManager;

import static java.lang.Thread.sleep;

public class Ec2ResourceManager implements CloudResourceManager {
    private static final Logger logger = LoggerFactory.getLogger(Ec2ResourceManager.class);
    private final MockAwsService mockAws;
    public Ec2ResourceManager(MockAwsService service) {
        this.mockAws = service;
    }

    public void create(Map<String,String> ec2Map, String subnetId, String groupId) throws InterruptedException {
        // Describe instances before creating a new instance
        if (!isDescribeInstancesBeforeCreationSuccess()){
            throw new RuntimeException("Failed requirements to create an Ec2 Instance");
        }
        // Create a request to launch an EC2 instance
        RunInstancesRequest runRequest = createRunInstancesRequest(ec2Map, subnetId, groupId);
        // Launch the instance
        RunInstancesResponse response = mockAws.runInstances(runRequest);
        Map<String, String> newEc2Data = handleInstanceLaunchResponse(response);
        if (newEc2Data == null){
            throw new RuntimeException("Failed to launch Ec2 instance");
        }

        Thread.sleep(2000);  //put a small delay in to check for ec2 creation time- it does take time
        // Describe instances after creating a new one
        if (!isDescribeInstancesAfterCreationSuccess(newEc2Data)){
            //ec2 not good - we should delete
            //no need to keep resources around

            //deteleEc2 - code not implemented
            throw new RuntimeException("Failed to create Ec2 instance");
        }
    }

    public void update(String ec2Id, String state) {
        if (state.equals(AwsConstants.STOP)) {
            StopInstancesRequest stopRequest = StopInstancesRequest.builder()
                    .instanceIds(ec2Id)
                    .build();

            mockAws.stopInstances(stopRequest);
            logger.info("Stopped Ec2 instance: " + ec2Id);
        }
    }

    private RunInstancesRequest createRunInstancesRequest(Map<String, String> ec2Map, String subnetId, String groupId) {
        return RunInstancesRequest.builder()
                .imageId(ec2Map.get("ami")) // Replace with a valid AMI ID
                .instanceType(ec2Map.get("instanceType"))
                .subnetId(subnetId)
                .networkInterfaces(InstanceNetworkInterfaceSpecification.builder()
                        .associatePublicIpAddress(true)
                        .deviceIndex(0)
                        .groups(groupId) // Include the security group
                        .build())
                .minCount(1)
                .maxCount(1)
                .build();
    }

    private Map<String, String> handleInstanceLaunchResponse(RunInstancesResponse response) {
        List<Instance> instances = response.instances();
        if (instances != null && !instances.isEmpty()) {
            String instanceId = instances.get(0).instanceId();
            String instanceType = instances.get(0).instanceTypeAsString();
            Map<String, String> newEc2Data = new HashMap<>();
            newEc2Data.put("id", instanceId);
            newEc2Data.put("instanceType", instanceType);
            logger.info("Successfully launched EC2 instance: " + instanceId + " of type: " + instanceType);
            return newEc2Data;
        } else {
            logger.error("Failed to launch EC2 instance.");
        }
        return null;
    }

    private boolean isDescribeInstancesBeforeCreationSuccess() {
        // Call the describeInstances method here to check for enough resources, etc
        // I'm not sure how to do that in detail
        // ec2s have unique id's - not sure if calling describe before creation is all that relevant
        return true;
    }

    private boolean isDescribeInstancesAfterCreationSuccess(Map<String, String> ec2Details) {
        // Call the describeInstances method here
        DescribeInstancesRequest request = DescribeInstancesRequest.builder()
                .instanceIds(Collections.singletonList(ec2Details.get("id")))
                .build(); // Add necessary filters or parameters
        DescribeInstancesResponse response = mockAws.describeInstances(request);
        logger.info("Instances after creation: " + response.reservations());

        boolean isRunning = false;
        for (Reservation reservation : response.reservations()) {
            for (Instance instance : reservation.instances()) {
                logger.info("Found instance: ID=" + instance.instanceId() + ", State=" + instance.state().name());
                if (instance.state().name() == InstanceStateName.RUNNING) {
                    isRunning = true;
                }
            }
        }
        return isRunning;

    }

    public boolean validate() {
        return true;
    }
}