package com.solace.cloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.configHandler.*;
import com.solace.cloud.aws.resource.*;
import com.solace.configHandler.aws.Properties;

public class CloudProviderFactory {
    public static void getResourceManager(CloudConfig cloudConfig) {
        switch (cloudConfig.getProvider().toLowerCase()) {
            case "aws":
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Properties properties = objectMapper.convertValue(cloudConfig.getProperties(), Properties.class);
                    AwsResourceFactory awsFactory = new AwsResourceFactory();
                    awsFactory.processResources(properties);
                } catch (Exception e) {
                    throw e;
                }

                break;
            //add others as necessary
            default:
                throw new IllegalArgumentException("Unknown provider: " + cloudConfig.getProvider().toLowerCase());
        }
    }
}
