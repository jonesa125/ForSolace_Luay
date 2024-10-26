package com.solace.cloud.aws.resource.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.ec2.model.*;
import com.solace.cloud.aws.service.MockAwsService;

import java.util.HashMap;
import java.util.Map;
import com.solace.cloud.CloudResourceManager;

public class VpcResourceManager implements CloudResourceManager<Vpc> {
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
        CreateSubnetResponse subnetResponse = createSubnet(vpcResponse.vpc().vpcId(), subnetCidrBlock, vpcDataMap.get("az_region"));


        CreateSecurityGroupResponse secGroupResponse = createSecurityGroup(vpcResponse.vpc().vpcId(), vpcDataMap.get("security_group"));

        if (vpcResponse !=null && subnetResponse != null && secGroupResponse != null) {
            Map<String, String> awsVpcDetails = new HashMap<>();
            awsVpcDetails.put("subnetId", subnetResponse.subnet().subnetId());
            awsVpcDetails.put("vpcId", vpcResponse.vpc().vpcId());
            awsVpcDetails.put("groupId", secGroupResponse.groupId());
            return awsVpcDetails;
        }
        return null;
    }

    private CreateSubnetResponse createSubnet(String vpcId, String subnetCidrBlock, String region) {
        CreateSubnetRequest subnetRequest = CreateSubnetRequest.builder()
                .vpcId(vpcId)
                .cidrBlock(subnetCidrBlock)
                .availabilityZone(region)
                .build();

        CreateSubnetResponse createSubnetResponse = mockAws.createSubnet(subnetRequest);

        // Output the subnet ID
        logger.info("Created subnet with ID: " + createSubnetResponse.subnet().subnetId());
        return createSubnetResponse;
    }

    private CreateSecurityGroupResponse createSecurityGroup(String vpcId, String secGroupName){
        CreateSecurityGroupRequest secGroupRequest = CreateSecurityGroupRequest.builder()
                .groupName(secGroupName)
                .vpcId(vpcId)
                .build();

        CreateSecurityGroupResponse createSecGroupResponse= mockAws.createSecurityGroup(secGroupRequest);

        // Output the subnet ID
        logger.info("Created security group with ID: " + createSecGroupResponse.groupId());
        return createSecGroupResponse;
    }

    public boolean validate() {
        return true;
    }

}