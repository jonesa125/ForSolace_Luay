package com.solace.cloud.aws.resource.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.ec2.model.CreateVpcRequest;
import software.amazon.awssdk.services.ec2.model.CreateVpcResponse;
import software.amazon.awssdk.services.ec2.model.CreateSubnetResponse;
import software.amazon.awssdk.services.ec2.model.CreateSubnetRequest;
import software.amazon.awssdk.services.ec2.model.Vpc;
import com.solace.cloud.aws.service.MockAwsService;
import java.util.Map;
import com.solace.cloud.CloudResourceManager;

public class VpcResourceManager implements CloudResourceManager<Vpc> {
    private static final Logger logger = LoggerFactory.getLogger("VpcResourceManager");
    private final MockAwsService mockAws;
    public VpcResourceManager(MockAwsService service) {
        this.mockAws = service;
    }

    public String create(Map<String,String> cidrBlockMap) {
        CreateVpcRequest request = CreateVpcRequest.builder()
                .cidrBlock(cidrBlockMap.get("vpc_cidr"))
                .build();
        String subnetCidrBlock = cidrBlockMap.get("subnet_cidr");

        CreateVpcResponse vpcResponse = mockAws.createVpc(request);
        // Output the VPC ID
        logger.info("Created VPC with ID: " + vpcResponse.vpc().vpcId());
        CreateSubnetResponse subnetResponse = createSubnet(vpcResponse.vpc().vpcId(), subnetCidrBlock, cidrBlockMap.get("az_region"));

        if (vpcResponse !=null && subnetResponse != null) {
            return subnetResponse.subnet().subnetId();
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

    public boolean validate() {
        return true;
    }

}