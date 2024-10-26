package com.solace.cloud.aws.resource;

import com.solace.cloud.CloudResourceFactory;
import com.solace.configHandler.PropertiesInterface;
import com.solace.configHandler.aws.Properties;
import com.solace.configHandler.aws.*;

import java.util.HashMap;
import java.util.Map;
import com.solace.cloud.aws.resource.manager.*;
import com.solace.cloud.aws.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AwsResourceFactory implements CloudResourceFactory {
    private static final Logger logger = LoggerFactory.getLogger(AwsResourceFactory.class);

    @Override
    public void processResources(PropertiesInterface props) {
        if (props != null) {
            Properties properties = (Properties) props;
            logger.info("action " + properties.getAction());
            String action = properties.getAction();
            if (!action.equals("create") && !action.equals("update") && !action.equals("delete")) {
                throw new IllegalArgumentException("Invalid action. The only supported options are create, update or delete");
            }
            if (action.equals("create")) {
                createResources(properties);
            } else if (action.equals("update")) {
                updateResources(properties);
            }
        }
    }

    private void createResources(Properties properties) {
        CasAwsService awsService = AwsServiceFactory.createAwsService(true);
        String region = properties.getRegion();
        logger.debug("region: " + region);

        Features features = properties.getFeatures();
        if (features != null) {
            try {
                Map<String, String> awsVpcDetails = createVpcResources(awsService, features, region);
                createEc2Resources(awsService, features, awsVpcDetails);
                createRdsResources(awsService, features, awsVpcDetails);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    private Map<String, String> createVpcResources(CasAwsService awsService, Features features, String region) {
        Map<String, String> awsVpcDetails = new HashMap<>();
        Map<String, String> vpcParamsMap = AwsResourceValidation.validateCreateVPCConfig(features.getVpc(), region);
        if (vpcParamsMap != null) {
            VpcResourceManager vpcManager = new VpcResourceManager((MockAwsService) awsService);
            awsVpcDetails = vpcManager.create(vpcParamsMap);
        }
        return awsVpcDetails;
    }

    private void createEc2Resources(CasAwsService awsService, Features features, Map<String, String> awsVpcDetails) {
        Map<String, String> ec2Props = AwsResourceValidation.validateCreateEC2Config(features.getEc2());
        if (ec2Props != null) {
            if (awsVpcDetails.isEmpty()) {
                throw new IllegalArgumentException("Abort: EC2 requires a VPC to be configured");
            }
            String subnetId = awsVpcDetails.get("subnetId");
            String groupId = awsVpcDetails.get("groupId");
            if (subnetId != null && groupId != null) {
                Ec2ResourceManager ec2Manager = new Ec2ResourceManager((MockAwsService) awsService);
                ec2Manager.create(ec2Props, subnetId, groupId);
            } else {
                throw new IllegalArgumentException("Abort: EC2 requires a public subnet and a security group. They are missing from config or failed to be created");
            }
        }
    }

    private void createRdsResources(CasAwsService awsService, Features features, Map<String, String> awsVpcDetails) {
        Map<String, String> rdsProps = AwsResourceValidation.validateCreateRDSConfig(features.getRds());
        if (awsVpcDetails.isEmpty()) {
            throw new IllegalArgumentException("Abort: RDS requires a VPC to be configured");
        }
        if (rdsProps != null) {
            String subnetId = awsVpcDetails.get("subnetId");
            String groupId = awsVpcDetails.get("groupId");
            if (subnetId != null && groupId != null) {
                RdsResourceManager rdsManager = new RdsResourceManager((MockAwsService) awsService);
                rdsManager.create(rdsProps, subnetId, groupId);
            } else {
                throw new IllegalArgumentException("Abort: RDS requires a public subnet and a security group name. They are missing from config or failed to be created");
            }
        }
    }

    private void updateResources(Properties properties) {
        MockAwsService awsService = AwsServiceFactory.createAwsService(true);
        String region = properties.getRegion();
        logger.debug("region: " + region);

        Features features = properties.getFeatures();
        if (features != null) {
            try {
                Map<String, String> ec2Props = AwsResourceValidation.validateUpdateEC2Config(features.getEc2());
                if (ec2Props != null) {
                    Ec2ResourceManager ec2Manager = new Ec2ResourceManager(awsService);
                    ec2Manager.update(ec2Props.get("id"), ec2Props.get("state"));
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

}