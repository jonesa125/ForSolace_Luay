package com.solace.cloud.aws.service;

import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.rds.model.*;
import software.amazon.awssdk.services.ec2.model.Subnet;
import java.util.Collections;
import java.util.List;


public class MockAwsService implements CasAwsService {
    @Override
    public CreateSecurityGroupResponse createSecurityGroup(CreateSecurityGroupRequest mockSecurityGroupRequest) {
        // Simulate a response
        return CreateSecurityGroupResponse.builder()
                .groupId("mock-secgroup-id")
                .build();
    }

    @Override
    public DescribeSecurityGroupsResponse describeSecurityGroups(DescribeSecurityGroupsRequest mockSecurityGroupRequest) {
        // Create a mock SecurityGroup
        SecurityGroup securityGroup = SecurityGroup.builder()
                .groupId(mockSecurityGroupRequest.groupIds().get(0))
                .groupName("mockSecurityGroup")
                .description("Mock description for security group")
                .build();
        return DescribeSecurityGroupsResponse.builder()
                .securityGroups(Collections.singletonList(securityGroup))
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
    public DescribeSubnetsResponse describeSubnets(DescribeSubnetsRequest mockSubnetRequest) {
        // Create a mock Subnet
        Subnet subnet = Subnet.builder()
                .subnetId(mockSubnetRequest.subnetIds().get(0))
                .cidrBlock("10.0.1.0/24")
                .vpcId("vpc-12345678")
                .availabilityZone("us-west-2a")
                .state("available")
                .build();

        // Return a DescribeSubnetsResponse containing the mock Subnet
        return DescribeSubnetsResponse.builder()
                .subnets(Collections.singletonList(subnet))
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
    public DescribeVpcsResponse describeVpcs(DescribeVpcsRequest request) {
        // Create a mock VPC
        Vpc vpc = Vpc.builder()
                .vpcId("vpcABCD")
                .cidrBlock("10.0.6.0/16")
                .state("available")
                .build();

        // Return a DescribeVpcsResponse containing the mock VPC
        return DescribeVpcsResponse.builder()
                .vpcs(Collections.singletonList(vpc))
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
    public DescribeInstancesResponse describeInstances(DescribeInstancesRequest mockInstanceRequest) {
        // Create a mock Instance
        Instance instance = Instance.builder()
                .instanceId(mockInstanceRequest.instanceIds().get(0))
                .instanceType("t2.micro")
                .state(state -> state.name("running"))
                .build();

        // Create a Reservation containing the Instance
        Reservation reservation = Reservation.builder()
                .instances(Collections.singletonList(instance))
                .build();

        // Return a DescribeInstancesResponse containing the mock Reservation
        return DescribeInstancesResponse.builder()
                .reservations(Collections.singletonList(reservation))
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

    @Override
    public DescribeDbInstancesResponse describeRdsInstances(DescribeDbInstancesRequest mockDbRequest) {
        // Create a mock DBInstance
        DBInstance dbInstance = DBInstance.builder()
                .dbInstanceIdentifier("db-id12345")
                .dbInstanceClass("db.t2.micro")
                .dbInstanceStatus("available")
                .engine("mysql")
                .endpoint(endpoint -> endpoint.address("mydbinstance.123456789012.us-east-2.rds.amazonaws.com"))
                .build();

        // Return a DescribeDBInstancesResponse containing the mock DBInstance
        return DescribeDbInstancesResponse.builder()
                .dbInstances(Collections.singletonList(dbInstance))
                .build();
    }
}