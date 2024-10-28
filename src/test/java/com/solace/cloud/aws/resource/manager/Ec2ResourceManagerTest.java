package com.solace.cloud.aws.resource.manager;


import com.solace.cloud.aws.AwsConstants;
import com.solace.cloud.aws.service.MockAwsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import software.amazon.awssdk.services.ec2.model.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Ec2ResourceManagerTest {

    @Mock
    private MockAwsService mockAws;

    @InjectMocks
    private Ec2ResourceManager ec2ResourceManager;

    private Map<String, String> ec2Map;
    private String subnetId;
    private String groupId;

    @BeforeEach
    void setUp() {
        ec2Map = new HashMap<>();
        ec2Map.put("ami", "ami-123456");
        ec2Map.put("instanceType", "t2.micro");
        subnetId = "subnet-123456";
        groupId = "sg-123456";
    }

    @Test
    void testCreate_Success() throws InterruptedException {
        // Given
        RunInstancesResponse runResponse = RunInstancesResponse.builder()
                .instances(Collections.singletonList(Instance.builder()
                        .instanceId("i-123456")
                        .instanceType("t2.micro")
                        .state(InstanceState.builder().name(InstanceStateName.RUNNING).build())
                        .build()))
                .build();

        when(mockAws.runInstances(any(RunInstancesRequest.class))).thenReturn(runResponse);
        when(mockAws.describeInstances(any(DescribeInstancesRequest.class)))
                .thenReturn(DescribeInstancesResponse.builder()
                        .reservations(Collections.singletonList(Reservation.builder()
                                .instances(Collections.singletonList(Instance.builder()
                                        .instanceId("i-123456")
                                        .state(InstanceState.builder().name(InstanceStateName.RUNNING).build())
                                        .build()))
                                .build()))
                        .build());

        // When
        ec2ResourceManager.create(ec2Map, subnetId, groupId);

        // Then
        verify(mockAws).runInstances(any(RunInstancesRequest.class));
        verify(mockAws).describeInstances(any(DescribeInstancesRequest.class));
    }

    @Test
    void testCreate_DescribeBeforeCreationFails() {
        // Given
        when(mockAws.runInstances(any(RunInstancesRequest.class)))
                .thenThrow(new RuntimeException("Failed requirements to create an Ec2 Instance"));

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            ec2ResourceManager.create(ec2Map, subnetId, groupId);
        });
        assertEquals("Failed requirements to create an Ec2 Instance", thrown.getMessage());
    }

    @Test
    void testCreate_InstanceLaunchFails() throws InterruptedException {
        // Given
        when(mockAws.runInstances(any(RunInstancesRequest.class))).thenReturn(RunInstancesResponse.builder().build());

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            ec2ResourceManager.create(ec2Map, subnetId, groupId);
        });
        assertEquals("Failed to launch Ec2 instance", thrown.getMessage());
    }

    @Test
    void testCreate_InstanceNotRunning() throws InterruptedException {
        // Given
        RunInstancesResponse runResponse = RunInstancesResponse.builder()
                .instances(Collections.singletonList(Instance.builder()
                        .instanceId("i-123456")
                        .instanceType("t2.micro")
                        .state(InstanceState.builder().name(InstanceStateName.STOPPED).build())
                        .build()))
                .build();

        when(mockAws.runInstances(any(RunInstancesRequest.class))).thenReturn(runResponse);
        when(mockAws.describeInstances(any(DescribeInstancesRequest.class)))
                .thenReturn(DescribeInstancesResponse.builder()
                        .reservations(Collections.singletonList(Reservation.builder()
                                .instances(Collections.singletonList(Instance.builder()
                                        .instanceId("i-123456")
                                        .state(InstanceState.builder().name(InstanceStateName.STOPPED).build())
                                        .build()))
                                .build()))
                        .build());

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            ec2ResourceManager.create(ec2Map, subnetId, groupId);
        });
        assertEquals("Failed to create Ec2 instance", thrown.getMessage());
    }

    @Test
    void testUpdate_StopInstance() {
        // Given
        String ec2Id = "i-123456";
        String state = AwsConstants.STOP;

        // When
        ec2ResourceManager.update(ec2Id, state);

        // Then
        verify(mockAws).stopInstances(StopInstancesRequest.builder().instanceIds(ec2Id).build());
    }
}