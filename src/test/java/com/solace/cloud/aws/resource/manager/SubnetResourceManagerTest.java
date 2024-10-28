package com.solace.cloud.aws.resource.manager;

import com.solace.cloud.aws.service.MockAwsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import software.amazon.awssdk.services.ec2.model.CreateSubnetRequest;
import software.amazon.awssdk.services.ec2.model.CreateSubnetResponse;
import software.amazon.awssdk.services.ec2.model.Subnet;

public class SubnetResourceManagerTest {

    private MockAwsService mockAws;
    private SubnetResourceManager subnetResourceManager;

    @BeforeEach
    void setUp() {
        mockAws = mock(MockAwsService.class);
        subnetResourceManager = new SubnetResourceManager(mockAws);
    }

    @Test
    void testCreateSubnet_Success() {
        String vpcId = "vpc-123456";
        String subnetCidrBlock = "10.0.1.0/24";
        String region = "us-east-1a";

        // Mocking the response from the AWS service
        Subnet mockSubnet = Subnet.builder()
                .subnetId("subnet-12345678")
                .build();
        CreateSubnetResponse mockResponse = CreateSubnetResponse.builder()
                .subnet(mockSubnet)
                .build();

        when(mockAws.createSubnet(any(CreateSubnetRequest.class))).thenReturn(mockResponse);

        // Call the create method
        CreateSubnetResponse response = subnetResourceManager.create(vpcId, subnetCidrBlock, region);

        // Verify that the createSubnet method was called
        ArgumentCaptor<CreateSubnetRequest> requestCaptor = ArgumentCaptor.forClass(CreateSubnetRequest.class);
        verify(mockAws).createSubnet(requestCaptor.capture());

        // Assert that the request parameters are correct
        assertEquals(vpcId, requestCaptor.getValue().vpcId());
        assertEquals(subnetCidrBlock, requestCaptor.getValue().cidrBlock());
        assertEquals(region, requestCaptor.getValue().availabilityZone());

        // Assert that the response is as expected
        assertNotNull(response);
        assertEquals("subnet-12345678", response.subnet().subnetId());
    }

    @Test
    void testValidate_ReturnsFalse() {
        // Test the validate method
        assertFalse(subnetResourceManager.validate());
    }
}
