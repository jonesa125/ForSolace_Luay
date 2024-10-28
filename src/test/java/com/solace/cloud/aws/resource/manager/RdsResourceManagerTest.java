package com.solace.cloud.aws.resource.manager;

import com.solace.cloud.aws.AwsConstants;
import com.solace.cloud.aws.service.MockAwsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import software.amazon.awssdk.services.rds.model.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RdsResourceManagerTest {

    private MockAwsService mockAws;
    private RdsResourceManager rdsResourceManager;

    @BeforeEach
    void setUp() {
        mockAws = mock(MockAwsService.class);
        rdsResourceManager = new RdsResourceManager(mockAws);
    }

    @Test
    void testCreateRds_Success() {
        Map<String, String> rdsTypes = new HashMap<>();
        rdsTypes.put("identifier", "test-db-instance");
        rdsTypes.put("instanceType", "db.t2.micro");
        rdsTypes.put("engine", "mysql");
        rdsTypes.put("username", "admin");
        rdsTypes.put("password", "password");
        rdsTypes.put("storage", "20");

        String subnetGroupName = "test-subnet-group";
        String groupId = "sg-0123456789abcdef0";

        String dbIdentifier = "abcd-tes111";
        // Mock the response to simulate the identifier already in use
        DBInstance existingInstance = DBInstance.builder()
                .dbInstanceIdentifier(dbIdentifier)
                .build();
        // Mock the availability check to return true
        when(mockAws.describeRdsInstances(any(DescribeDbInstancesRequest.class)))
                .thenReturn(DescribeDbInstancesResponse.builder().dbInstances(Collections.singletonList(existingInstance)).build());

        // Mocking the response from the AWS service
        CreateDbInstanceResponse mockResponse = CreateDbInstanceResponse.builder().build();
        when(mockAws.createRdsInstance(any(CreateDbInstanceRequest.class))).thenReturn(mockResponse);

        // Call the create method
        rdsResourceManager.create(rdsTypes, subnetGroupName, groupId);

        // Verify that createRdsInstance was called
        ArgumentCaptor<CreateDbInstanceRequest> requestCaptor = ArgumentCaptor.forClass(CreateDbInstanceRequest.class);
        verify(mockAws).createRdsInstance(requestCaptor.capture());
    }

    @Test
    void testIsDbInstanceIdentifierAvailable_AlreadyInUse() {
        String dbIdentifier = "test-db-instance";

        // Mock the response to simulate the identifier already in use
        DBInstance existingInstance = DBInstance.builder()
                .dbInstanceIdentifier(dbIdentifier)
                .build();
        DescribeDbInstancesResponse response = DescribeDbInstancesResponse.builder()
                .dbInstances(Collections.singletonList(existingInstance))
                .build();

        when(mockAws.describeRdsInstances(any(DescribeDbInstancesRequest.class))).thenReturn(response);

        // Call the method and assert it returns false
        boolean isAvailable = rdsResourceManager.isDbInstanceIdentifierAvailable(dbIdentifier);
        assertFalse(isAvailable);
    }

    @Test
    void testIsDbInstanceIdentifierAvailable_Available() {
        String dbIdentifier = "new-db-instance";

        // Mock the response to simulate no existing instance
        DescribeDbInstancesResponse response = DescribeDbInstancesResponse.builder()
                .dbInstances(Collections.emptyList())
                .build();

        when(mockAws.describeRdsInstances(any(DescribeDbInstancesRequest.class))).thenReturn(response);

        // Call the method and assert it returns true
        boolean isAvailable = rdsResourceManager.isDbInstanceIdentifierAvailable(dbIdentifier);
        assertTrue(isAvailable);
    }

    @Test
    void testConfirmRdsCreation_Success() {
        String dbIdentifier = "test-db-instance";

        // Mock the response to simulate the instance being found
        DBInstance existingInstance = DBInstance.builder()
                .dbInstanceIdentifier(dbIdentifier)
                .dbInstanceStatus("available")
                .build();
        DescribeDbInstancesResponse response = DescribeDbInstancesResponse.builder()
                .dbInstances(Collections.singletonList(existingInstance))
                .build();

        when(mockAws.describeRdsInstances(any(DescribeDbInstancesRequest.class))).thenReturn(response);

        // Call the method and check for successful completion
        rdsResourceManager.confirmRdsCreation(dbIdentifier);
    }

    @Test
    void testConfirmRdsCreation_NotFound() {
        String dbIdentifier = "non-existent-db-instance";

        // Mock the response to simulate the instance not being found
        DescribeDbInstancesResponse response = DescribeDbInstancesResponse.builder()
                .dbInstances(Collections.emptyList())
                .build();

        when(mockAws.describeRdsInstances(any(DescribeDbInstancesRequest.class))).thenReturn(response);

        // Call the method and expect an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rdsResourceManager.confirmRdsCreation(dbIdentifier);
        });

        assertEquals("Error: RDS instance not found after creation!", exception.getMessage());
    }

    @Test
    void testUpdateRdsInstance_Stop() {
        String dbId = "test-db-instance";

        // Call the update method to stop the RDS instance
        rdsResourceManager.update(dbId, AwsConstants.STOP);

        // Verify that the stopRdsInstance method was called with the correct parameter
        ArgumentCaptor<StopDbInstanceRequest> requestCaptor = ArgumentCaptor.forClass(StopDbInstanceRequest.class);
        verify(mockAws).stopRdsInstance(requestCaptor.capture());

        assertEquals(dbId, requestCaptor.getValue().dbInstanceIdentifier());
    }
}
