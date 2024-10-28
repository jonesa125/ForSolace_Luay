package com.solace.cloud.aws.resource.manager;

import com.solace.cloud.aws.service.MockAwsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import software.amazon.awssdk.services.ec2.model.*;

public class VpcResourceManagerTest {
    @Mock
    private MockAwsService mockAws;
    @InjectMocks
    private VpcResourceManager vpcResourceManager;

    @BeforeEach
    void setUp() {
        mockAws = mock(MockAwsService.class);
        vpcResourceManager = new VpcResourceManager(mockAws);
    }

    @Test
    void testCreateVpc_Success() {
        Map<String, String> vpcDataMap = new HashMap<>();
        vpcDataMap.put("vpc_cidr", "10.0.0.0/16");
        vpcDataMap.put("subnet_cidr", "10.0.1.0/24");
        vpcDataMap.put("security_group", "test-sg");
        vpcDataMap.put("az_region", "us-east-1a");

        // Mock responses
        Vpc mockVpc = Vpc.builder().vpcId("vpc-12345678").cidrBlock("10.0.76.0/16").state("available").build();
        CreateVpcResponse createVpcResponse = CreateVpcResponse.builder().vpc(mockVpc).build();
        when(mockAws.createVpc(any(CreateVpcRequest.class))).thenReturn(createVpcResponse);

        // Mock subnet creation
        CreateSubnetResponse mockSubnetResponse = CreateSubnetResponse.builder()
                .subnet(Subnet.builder().subnetId("subnet-12345678").build())
                .build();
        SubnetResourceManager subnetManager = new SubnetResourceManager(mockAws);
        when(mockAws.createSubnet(any(CreateSubnetRequest.class))).thenReturn(mockSubnetResponse);

        // Mock security group creation
        CreateSecurityGroupResponse mockSecurityGroupResponse = CreateSecurityGroupResponse.builder()
                .groupId("sg-12345678")
                .build();
        SecurityGroupResourceManager sgManager = new SecurityGroupResourceManager(mockAws);
        when(mockAws.createSecurityGroup(any(CreateSecurityGroupRequest.class))).thenReturn(mockSecurityGroupResponse);

        DescribeVpcsRequest describeRequest = DescribeVpcsRequest.builder().build();

        DescribeVpcsResponse describeResponse = DescribeVpcsResponse.builder()
                .vpcs(mockVpc)
                .build();

        when(mockAws.describeVpcs(any(DescribeVpcsRequest.class))).thenReturn(describeResponse);

        // Call the create method
        Map<String, String> awsVpcDetails = vpcResourceManager.create(vpcDataMap);

        // Verify that the createVpc method was called
        ArgumentCaptor<CreateVpcRequest> vpcRequestCaptor = ArgumentCaptor.forClass(CreateVpcRequest.class);
        verify(mockAws).createVpc(vpcRequestCaptor.capture());
        assertEquals("10.0.0.0/16", vpcRequestCaptor.getValue().cidrBlock());

        // Assert the returned details
        assertNotNull(awsVpcDetails);
        assertEquals("subnet-12345678", awsVpcDetails.get("subnetId"));
        assertEquals("vpc-12345678", awsVpcDetails.get("vpcId"));
        assertEquals("sg-12345678", awsVpcDetails.get("groupId"));
    }

    @Test
    void testCreateVpc_CidrBlockInUse() {
        Map<String, String> vpcDataMap = new HashMap<>();
        vpcDataMap.put("vpc_cidr", "10.0.0.0/16");

        // Mock the response for existing CIDR block
        DescribeVpcsResponse describeResponse = DescribeVpcsResponse.builder()
                .vpcs(Vpc.builder().cidrBlock("10.0.0.0/16").build())
                .build();
        when(mockAws.describeVpcs(any(DescribeVpcsRequest.class))).thenReturn(describeResponse);

        // Assert exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vpcResourceManager.create(vpcDataMap);
        });
        assertEquals("CIDR block 10.0.0.0/16is already in use", exception.getMessage());
    }

    @Test
    void testConfirmVpcCreation_VpcNotFound() {
        String vpcId = "vpc-12345678";
        // Create a mock VPC
        Vpc vpc = Vpc.builder()
                .vpcId(vpcId)
                .cidrBlock("10.0.6.0/16")
                .state("available")
                .build();
        // Mock response for VPC not found
        DescribeVpcsResponse describeResponse = DescribeVpcsResponse.builder().vpcs(Collections.singletonList(vpc)).build();
        when(mockAws.describeVpcs(any(DescribeVpcsRequest.class))).thenReturn(describeResponse);

        // Confirming VPC creation should log a warning (you can verify logging if needed)
        vpcResourceManager.confirmVpcCreation(vpcId);
    }


    void testValidate_ReturnsTrue() {
        assertTrue(vpcResourceManager.validate());
    }
}
