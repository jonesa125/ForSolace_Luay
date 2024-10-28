package com.solace.cloud.aws.resource.manager;

import com.solace.cloud.CloudResourceManager;
import com.solace.cloud.aws.service.MockAwsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.ec2.model.CreateSubnetRequest;
import software.amazon.awssdk.services.ec2.model.CreateSubnetResponse;

public class SubnetResourceManager implements CloudResourceManager {
    private static final Logger logger = LoggerFactory.getLogger(SubnetResourceManager.class);
    private final MockAwsService mockAws;

    public SubnetResourceManager(MockAwsService service) {
        this.mockAws = service;
    }

    public CreateSubnetResponse create(String vpcId, String subnetCidrBlock, String region) {
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

    @Override
    public boolean validate() {
        return false;
    }
}
