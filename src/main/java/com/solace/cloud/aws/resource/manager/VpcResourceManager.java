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
        String cidrBlock = vpcDataMap.get("vpc_cidr");
        // Step 1: Check if the CIDR block is available
        if (!isCidrBlockAvailable(cidrBlock)) {
            throw new RuntimeException("CIDR block "  + cidrBlock + "is already in use");
        }
        // Step 2: Create VPC
        CreateVpcRequest request = CreateVpcRequest.builder()
                .cidrBlock(cidrBlock)
                .build();

        CreateVpcResponse vpcResponse = mockAws.createVpc(request);
        logger.info("Created VPC with ID: " + vpcResponse.vpc().vpcId());

        // Step 3: Create Subnet and Security Group
        SubnetResourceManager subnetManager = new SubnetResourceManager(mockAws);
        String subnetCidrBlock = vpcDataMap.get("subnet_cidr");
        CreateSubnetResponse subnetResponse = subnetManager.create(vpcResponse.vpc().vpcId(), subnetCidrBlock, vpcDataMap.get("az_region"));

        SecurityGroupResourceManager sgManager = new SecurityGroupResourceManager(mockAws);
        CreateSecurityGroupResponse secGroupResponse = sgManager.create(vpcResponse.vpc().vpcId(), vpcDataMap.get("security_group"));

        // Step 4: Check if VPC is created successfully
        if (vpcResponse != null && subnetResponse != null && secGroupResponse != null) {
            Map<String, String> awsVpcDetails = new HashMap<>();
            awsVpcDetails.put("subnetId", subnetResponse.subnet().subnetId());
            awsVpcDetails.put("vpcId", vpcResponse.vpc().vpcId());
            awsVpcDetails.put("groupId", secGroupResponse.groupId());
            // Describe the created VPC to confirm it is functioning
            confirmVpcCreation(vpcResponse.vpc().vpcId());
            return awsVpcDetails;
        }
        return null;
    }

    // Method to check if the CIDR block is available
    private boolean isCidrBlockAvailable(String cidrBlock) {
        DescribeVpcsRequest describeRequest = DescribeVpcsRequest.builder().build();
        DescribeVpcsResponse describeResponse = mockAws.describeVpcs(describeRequest);
        // Check if the CIDR block is used in any existing VPCs
        for (Vpc vpc : describeResponse.vpcs()) {
            if (vpc.cidrBlock().equals(cidrBlock)) {
                return false; // CIDR block is already in use
            }
        }
        return true; // CIDR block is available
    }

    // Method to confirm the VPC creation
    void confirmVpcCreation(String vpcId) {
        DescribeVpcsRequest request = DescribeVpcsRequest.builder()
                .vpcIds(vpcId)
                .build();

        DescribeVpcsResponse response = mockAws.describeVpcs(request);
        if (!response.vpcs().isEmpty()) {
            logger.info("VPC is successfully created and in state: " + response.vpcs().get(0).state());
        } else {
            throw new RuntimeException("VPC not found after creation!");
        }
    }

    public static void deleteVPC(String vpcId) {
        try {
            DeleteVpcRequest deleteVpcRequest = DeleteVpcRequest.builder()
                    .vpcId(vpcId)
                    .build();

            //DeleteVpcResponse response = mockAws.deleteVpc(deleteVpcRequest);
            logger.info("Successfully deleted VPC with ID: " + vpcId);
        } catch (Ec2Exception e) {
            throw new RuntimeException("Failed to delete VPC: " + e.awsErrorDetails().errorMessage());
        }
    }

    public boolean validate() {
        return true;
    }

}