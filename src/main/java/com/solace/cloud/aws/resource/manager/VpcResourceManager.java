package com.solace.cloud.aws.resource.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.ec2.model.*;
import com.solace.cloud.aws.service.MockAwsService;

import java.util.HashMap;
import java.util.Map;
import com.solace.cloud.CloudResourceManager;

public class VpcResourceManager implements CloudResourceManager {
    private static final Logger logger = LoggerFactory.getLogger(VpcResourceManager.class);
    private final MockAwsService mockAws;
    public VpcResourceManager(MockAwsService service) {
        this.mockAws = service;
    }

    public Map<String,String> create(Map<String,String> vpcDataMap) {
        CreateVpcRequest request = CreateVpcRequest.builder()
                .cidrBlock(vpcDataMap.get("vpc_cidr"))
                .build();
        String subnetCidrBlock = vpcDataMap.get("subnet_cidr");

        CreateVpcResponse vpcResponse = mockAws.createVpc(request);
        // Output the VPC ID
        logger.info("Created VPC with ID: " + vpcResponse.vpc().vpcId());
        SubnetResourceManager subnetManager = new SubnetResourceManager(mockAws);
        CreateSubnetResponse subnetResponse = subnetManager.create(vpcResponse.vpc().vpcId(), subnetCidrBlock, vpcDataMap.get("az_region"));

        SecurityGroupResourceManager sgManager = new SecurityGroupResourceManager(mockAws);
        CreateSecurityGroupResponse secGroupResponse = sgManager.create(vpcResponse.vpc().vpcId(), vpcDataMap.get("security_group"));

        if (vpcResponse !=null && subnetResponse != null && secGroupResponse != null) {
            Map<String, String> awsVpcDetails = new HashMap<>();
            awsVpcDetails.put("subnetId", subnetResponse.subnet().subnetId());
            awsVpcDetails.put("vpcId", vpcResponse.vpc().vpcId());
            awsVpcDetails.put("groupId", secGroupResponse.groupId());
            return awsVpcDetails;
        }
        return null;
    }

    public boolean validate() {
        return true;
    }

}