package com.solace.cloud;

import com.solace.cloud.aws.resource.AwsResourceFactory;
import com.solace.configHandler.*;
import com.solace.configHandler.aws.Properties;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class CloudProviderFactoryTest {

    @Test
    public void testGetResourceManagerWithAws() {
        // Arrange
        CloudConfig cloudConfig = mock(CloudConfig.class);
        when(cloudConfig.getProvider()).thenReturn("aws");
        when(cloudConfig.getProperties()).thenReturn(Map.of("action", "create"));

        // Act
        try (MockedStatic<AwsResourceFactory> mockedStatic = mockStatic(AwsResourceFactory.class)) {
            CloudProviderFactory.getResourceManager(cloudConfig);

            Properties properties = new Properties();
            properties.setAction("create");
            // Assert
            AwsResourceFactory awsFactory = new AwsResourceFactory();
            mockedStatic.verify(() -> awsFactory.processResources(properties));
        }
    }

    @Test
    public void testGetResourceManagerWithUnknownProvider() {
        // Arrange
        CloudConfig cloudConfig = mock(CloudConfig.class);
        when(cloudConfig.getProvider()).thenReturn("unknown");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CloudProviderFactory.getResourceManager(cloudConfig);
        });
        assertEquals("Unknown provider: unknown", exception.getMessage());
    }


}