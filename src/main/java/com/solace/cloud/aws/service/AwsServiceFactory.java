package com.solace.cloud.aws.service;

public class AwsServiceFactory {
    public static MockAwsService createAwsService(boolean useMock) {
        if (useMock) {
            return new MockAwsService();
        } else {
            //Real service setup
        }
        return null;
    }
}
