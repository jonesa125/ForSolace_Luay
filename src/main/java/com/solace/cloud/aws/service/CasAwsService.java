package com.solace.cloud.aws.service;

import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupRequest;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupResponse;
import software.amazon.awssdk.services.ec2.model.CreateSubnetRequest;
import software.amazon.awssdk.services.ec2.model.CreateSubnetResponse;
import software.amazon.awssdk.services.ec2.model.CreateVpcRequest;
import software.amazon.awssdk.services.ec2.model.CreateVpcResponse;
import software.amazon.awssdk.services.ec2.model.RunInstancesResponse;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.StopInstancesResponse;
import software.amazon.awssdk.services.ec2.model.StopInstancesRequest;
import software.amazon.awssdk.services.rds.model.CreateDbInstanceResponse;
import software.amazon.awssdk.services.rds.model.CreateDbInstanceRequest;
import software.amazon.awssdk.services.rds.model.StopDbInstanceRequest;
import software.amazon.awssdk.services.rds.model.StopDbInstanceResponse;

public interface CasAwsService {
    CreateVpcResponse createVpc(CreateVpcRequest vpcRequest);
    CreateSubnetResponse createSubnet(CreateSubnetRequest request);
    CreateSecurityGroupResponse createSecurityGroup(CreateSecurityGroupRequest request);
    RunInstancesResponse runInstances(RunInstancesRequest request);
    StopInstancesResponse stopInstances(StopInstancesRequest request);
    CreateDbInstanceResponse createRdsInstance(CreateDbInstanceRequest request);
    StopDbInstanceResponse stopRdsInstance(StopDbInstanceRequest mockDbRequest);
}