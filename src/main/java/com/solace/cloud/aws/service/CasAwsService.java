package com.solace.cloud.aws.service;

import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.rds.model.*;

public interface CasAwsService {
    CreateVpcResponse createVpc(CreateVpcRequest vpcRequest);
    DescribeVpcsResponse describeVpcs(DescribeVpcsRequest request);
    CreateSubnetResponse createSubnet(CreateSubnetRequest request);
    DescribeSubnetsResponse describeSubnets(DescribeSubnetsRequest request);
    CreateSecurityGroupResponse createSecurityGroup(CreateSecurityGroupRequest request);
    DescribeSecurityGroupsResponse describeSecurityGroups(DescribeSecurityGroupsRequest request);
    RunInstancesResponse runInstances(RunInstancesRequest request);
    StopInstancesResponse stopInstances(StopInstancesRequest request);
    DescribeInstancesResponse describeInstances(DescribeInstancesRequest request);
    CreateDbInstanceResponse createRdsInstance(CreateDbInstanceRequest request);
    StopDbInstanceResponse stopRdsInstance(StopDbInstanceRequest mockDbRequest);
    DescribeDbInstancesResponse describeRdsInstances(DescribeDbInstancesRequest request);
}