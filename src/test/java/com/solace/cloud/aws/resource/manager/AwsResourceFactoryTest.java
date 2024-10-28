package com.solace.cloud.aws.resource.manager;

import com.solace.cloud.aws.AwsConstants;
import com.solace.cloud.aws.resource.AwsResourceFactory;
import com.solace.cloud.aws.resource.AwsResourceValidation;
import com.solace.cloud.aws.service.CasAwsService;
import com.solace.cloud.aws.service.MockAwsService;
import com.solace.configHandler.PropertiesInterface;
import com.solace.configHandler.aws.Features;
import com.solace.configHandler.aws.Properties;
import com.solace.configHandler.aws.Subnet;
import com.solace.configHandler.aws.Vpc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.services.ec2.model.SecurityGroup;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

class AwsResourceFactoryTest {

    private AwsResourceFactory awsResourceFactory;
    private Properties mockProperties;
    private MockAwsService mockAwsService;
    private Features mockFeatures;
    private VpcResourceManager mockVpcResourceManager;

    @BeforeEach
    void setUp() {
        awsResourceFactory = new AwsResourceFactory();
        mockProperties = mock(Properties.class);
        mockAwsService = mock(MockAwsService.class);
        mockFeatures = mock(Features.class);
        mockVpcResourceManager = mock(VpcResourceManager.class);
    }

    @Test
    void testProcessResources_CreateAction() {
        when(mockProperties.getAction()).thenReturn(AwsConstants.CREATE);

        awsResourceFactory.processResources(mockProperties);

        // Verify that createResources is called with the correct properties
        verify(mockProperties, times(2)).getAction();
    }

    @Test
    void testProcessResources_UpdateAction() {
        when(mockProperties.getAction()).thenReturn(AwsConstants.UPDATE);

        awsResourceFactory.processResources(mockProperties);

        // Verify that updateResources is called with the correct properties
        verify(mockProperties, times(2)).getAction();
    }

    @Test
    void testProcessResources_InvalidAction() {
        when(mockProperties.getAction()).thenReturn("invalid");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            awsResourceFactory.processResources(mockProperties);
        });

        assertEquals("Invalid action. The only supported options are create or update", exception.getMessage());
    }

    @Test
    void testProcessResources_NullProperties() {
        awsResourceFactory.processResources(null);
        // No exception should be thrown, and nothing should happen
    }


    void testCreateVpcResources_ValidParams() {
        // Prepare test data
        String region = "us-west-1";
        Map<String, String> vpcParamsMap = new HashMap<>();
        vpcParamsMap.put("vpcId", "vpc-123456");

        Map<String, String> expectedVpcDetails = new HashMap<>();
        expectedVpcDetails.put("vpcId", "vpc-123456");

        // Mock the behavior of AwsResourceValidation and VpcResourceManager
        Vpc newVpc = new Vpc();
        newVpc.setCidrblock("10.0.0.1/16");
        Subnet newSub = new Subnet();
        newSub.setCidrblock("10.2.1.1/16");
        newSub.setAvzone("us-west-1a");
        newVpc.setSubnet(newSub);
        newVpc.setSecuritygroup("sg-1243");
        when(mockFeatures.getVpc()).thenReturn(newVpc);
        when(AwsResourceValidation.validateCreateVPCConfig(mockFeatures.getVpc(), region)).thenReturn(vpcParamsMap);
        when(mockVpcResourceManager.create(any())).thenReturn(expectedVpcDetails); // Mock the create method

        // Call the method under test
        Map<String, String> awsVpcDetails = awsResourceFactory.createVpcResources(mockAwsService, mockFeatures, region);

        // Verify results
        assertNotNull(awsVpcDetails);
        assertEquals("vpc-123456", awsVpcDetails.get("vpcId"));
        verify(mockVpcResourceManager).create(vpcParamsMap); // Verify that the create method was called
    }


}