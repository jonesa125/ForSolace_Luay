package com.solace.cloud.aws.resource.manager;

import com.solace.cloud.CloudResourceManager;
import com.solace.cloud.aws.service.MockAwsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupRequest;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupResponse;

public class SecurityGroupResourceManager implements CloudResourceManager {
    private static final Logger logger = LoggerFactory.getLogger(SecurityGroupResourceManager.class);
    private final MockAwsService mockAws;

    public SecurityGroupResourceManager(MockAwsService service) {
        this.mockAws = service;
    }
    public CreateSecurityGroupResponse create(String vpcId, String secGroupName){
        CreateSecurityGroupRequest secGroupRequest = CreateSecurityGroupRequest.builder()
                .groupName(secGroupName)
                .vpcId(vpcId)
                .build();

        CreateSecurityGroupResponse createSecGroupResponse= mockAws.createSecurityGroup(secGroupRequest);

        // Output the subnet ID
        logger.info("Created security group with ID: " + createSecGroupResponse.groupId());
        return createSecGroupResponse;
    }

    @Override
    public boolean validate() {
        return false;
    }
}
