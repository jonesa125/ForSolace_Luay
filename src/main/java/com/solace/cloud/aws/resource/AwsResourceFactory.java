package com.solace.cloud.aws.resource;

import com.solace.cloud.CloudResourceFactory;
import com.solace.configHandler.PropertiesInterface;
import com.solace.configHandler.aws.Properties;
import com.solace.configHandler.aws.*;
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
        MockAwsService awsService = AwsServiceFactory.createAwsService(true);
        String region = properties.getRegion();
        logger.info("region: " + region);

        Features features = properties.getFeatures();
        if (features != null) {
            try {
                String subnetId = null;
                Map<String, String> vpcParamsMap = AwsResourceValidation.validateCreateVPCConfig(features.getVpc(), region);
                if (vpcParamsMap != null) {
                    VpcResourceManager vpcManager = new VpcResourceManager(awsService);
                    subnetId = vpcManager.create(vpcParamsMap);
                }

                Map<String, String> ec2Props = AwsResourceValidation.validateCreateEC2Config(features.getEc2());
                if (ec2Props != null) {
                    if (subnetId != null) {
                        Ec2ResourceManager ec2Manager = new Ec2ResourceManager(awsService);
                        ec2Manager.create(ec2Props, subnetId);
                    } else {
                        throw new IllegalArgumentException("Abort: Ec2 requires a public subnet. The subnet id is missing from config or failed to be created");
                    }
                }

                Map<String, String> rdsProps = AwsResourceValidation.validateCreateRDSConfig(features.getRds());
                if (rdsProps != null) {
                    RdsResourceManager rdsManager = new RdsResourceManager(awsService);
                    rdsManager.create(rdsProps);
                }

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void updateResources(Properties properties) {
        MockAwsService awsService = AwsServiceFactory.createAwsService(true);
        String region = properties.getRegion();
        logger.info("region: " + region);

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