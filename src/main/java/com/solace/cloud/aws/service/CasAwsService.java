package com.solace.cloud.aws.service;

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

public interface CasAwsService {
    CreateVpcResponse createVpc(CreateVpcRequest vpcRequest);
    CreateSubnetResponse createSubnet(CreateSubnetRequest request);
    RunInstancesResponse runInstances(RunInstancesRequest request);
    StopInstancesResponse stopInstances(StopInstancesRequest request);
    CreateDbInstanceResponse createRdsInstance(CreateDbInstanceRequest request);
}