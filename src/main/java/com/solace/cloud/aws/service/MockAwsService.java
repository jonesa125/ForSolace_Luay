package com.solace.cloud.aws.service;

import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.rds.model.*;

import java.util.Collections;


public class MockAwsService implements CasAwsService {
    @Override
    public CreateSecurityGroupResponse createSecurityGroup(CreateSecurityGroupRequest mockSecurityGroupRequest) {
        // Simulate a response
        return CreateSecurityGroupResponse.builder()
                .groupId("mock-secgroup-id")
                .build();
    }


    @Override
    public CreateSubnetResponse createSubnet(CreateSubnetRequest mockSubnetRequest) {
        // Simulate a response
        return CreateSubnetResponse.builder()
                .subnet(mockSubnet -> mockSubnet.subnetId("mock-subnet-id"))
                .build();
    }

    @Override
    public CreateVpcResponse createVpc(CreateVpcRequest mockVpcRequest) {
        // Simulate a response
        return CreateVpcResponse.builder()
                .vpc(mockVpcResp -> mockVpcResp.vpcId("dasfdas"))
                .build();
    }

    @Override
    public RunInstancesResponse runInstances(RunInstancesRequest mockInstanceRequest) {
        // Simulate a response
        Instance mockInstance = Instance.builder()
                .instanceId("i-imadethisec2")
                .instanceType("t2.micro-slowcomputer")
                .build();

        return RunInstancesResponse.builder()
                .instances(mockInstance)
                .build();
    }

    @Override
    public StopInstancesResponse stopInstances(StopInstancesRequest mockInstanceRequest) {
        // Simulate a response
        InstanceStateChange mockInstance = InstanceStateChange.builder()
                .instanceId(mockInstanceRequest.instanceIds().get(0))
                .currentState(InstanceState.builder()
                        .name("stopping") // Set the current state
                        .build())
                .previousState(InstanceState.builder()
                        .name("running") // Set the previous state
                        .build())
                .build();

        return StopInstancesResponse.builder()
                .stoppingInstances(Collections.singletonList(mockInstance))
                .build();
    }

    @Override
    public CreateDbInstanceResponse createRdsInstance(CreateDbInstanceRequest mockDbRequest) {
        // Simulate a response
        DBInstance mockDbInstance = DBInstance.builder()
                .dbInstanceIdentifier(mockDbRequest.dbInstanceIdentifier())
                .build();

        return CreateDbInstanceResponse.builder()
                .dbInstance(mockDbInstance)
                .build();
    }

    @Override
    public StopDbInstanceResponse stopRdsInstance(StopDbInstanceRequest mockDbRequest) {
        // Simulate a response
        DBInstance mockDbInstance = DBInstance.builder()
                .dbInstanceIdentifier(mockDbRequest.dbInstanceIdentifier())
                .build();

        return StopDbInstanceResponse.builder()
                .dbInstance(mockDbInstance)
                .build();
    }
}