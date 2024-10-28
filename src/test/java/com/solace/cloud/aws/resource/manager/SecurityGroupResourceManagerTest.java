package com.solace.cloud.aws.resource.manager;
import com.solace.cloud.aws.service.MockAwsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupRequest;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupResponse;



public class SecurityGroupResourceManagerTest {

    private MockAwsService mockAws;
    private SecurityGroupResourceManager securityGroupResourceManager;

    @BeforeEach
    void setUp() {
        mockAws = mock(MockAwsService.class);
        securityGroupResourceManager = new SecurityGroupResourceManager(mockAws);
    }

    @Test
    void testCreateSecurityGroup_Success() {
        String vpcId = "vpc-123456";
        String secGroupName = "test-security-group";

        // Mocking the response from the AWS service
        CreateSecurityGroupResponse mockResponse = CreateSecurityGroupResponse.builder()
                .groupId("sg-12345678")
                .build();
        when(mockAws.createSecurityGroup(any(CreateSecurityGroupRequest.class))).thenReturn(mockResponse);

        // Call the create method
        CreateSecurityGroupResponse response = securityGroupResourceManager.create(vpcId, secGroupName);

        // Verify that the createSecurityGroup method was called
        ArgumentCaptor<CreateSecurityGroupRequest> requestCaptor = ArgumentCaptor.forClass(CreateSecurityGroupRequest.class);
        verify(mockAws).createSecurityGroup(requestCaptor.capture());

        // Assert that the request parameters are correct
        assertEquals(vpcId, requestCaptor.getValue().vpcId());
        assertEquals(secGroupName, requestCaptor.getValue().groupName());

        // Assert that the response is as expected
        assertNotNull(response);
        assertEquals("sg-12345678", response.groupId());
    }

    @Test
    void testValidate_ReturnsFalse() {
        // Test the validate method
        assertFalse(securityGroupResourceManager.validate());
    }
}
