package com.solace.cloud;

import com.solace.cloud.aws.resource.AwsResourceFactory;
import com.solace.configHandler.*;
import com.solace.configHandler.aws.Properties;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

public class CloudProviderFactoryTest {

    @Test
    void testGetResourceManager_WithAwsProvider() throws Exception {
        // Arrange
        CloudConfig cloudConfig = new CloudConfig();
        cloudConfig.setProvider("aws");
        cloudConfig.setProperties(Map.of("action", "create"));
        // Mock construction of AwsResourceFactory
        try (MockedConstruction<AwsResourceFactory> mockedConstruction = mockConstruction(AwsResourceFactory.class)) {
            // Act
            CloudProviderFactory.getResourceManager(cloudConfig);

            // Get the constructed instance from the mocked construction
            AwsResourceFactory awsFactory = mockedConstruction.constructed().get(0);

            // Assert
            Properties properties = new Properties();
            properties.setAction("create");
            verify(awsFactory).processResources(properties);
        }
    }

    @Test
    void testGetResourceManager_WithUnknownProvider() {
        // Arrange
        CloudConfig cloudConfig = new CloudConfig();
        cloudConfig.setProvider("unknown");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CloudProviderFactory.getResourceManager(cloudConfig);
        });
        assertEquals("Unknown provider: unknown", exception.getMessage());
    }

}