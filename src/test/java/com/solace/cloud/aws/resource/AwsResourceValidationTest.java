package com.solace.cloud.aws.resource;

import com.solace.configHandler.aws.Rds;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
public class AwsResourceValidationTest {

    @Test
    void testValidateCreateRDSConfig_NullRds() {
        assertNull(AwsResourceValidation.validateCreateRDSConfig(null));
    }

    @Test
    void testValidateCreateRDSConfig_MissingInstType() {
        Rds rds = mock(Rds.class);
        when(rds.getInsttype()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> AwsResourceValidation.validateCreateRDSConfig(rds), "rds Instance type is missing");
    }

    @Test
    void testValidateCreateRDSConfig_MissingIdentifier() {
        Rds rds = mock(Rds.class);
        when(rds.getInsttype()).thenReturn("type");
        when(rds.getIdentifier()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> AwsResourceValidation.validateCreateRDSConfig(rds), "rds identifier is missing");
    }

    @Test
    void testValidateCreateRDSConfig_MissingStorage() {
        Rds rds = mock(Rds.class);
        when(rds.getInsttype()).thenReturn("type");
        when(rds.getIdentifier()).thenReturn("id");
        when(rds.getStorage()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> AwsResourceValidation.validateCreateRDSConfig(rds), "rds storage is missing");
    }

    @Test
    void testValidateCreateRDSConfig_MissingEngine() {
        Rds rds = mock(Rds.class);
        when(rds.getInsttype()).thenReturn("type");
        when(rds.getIdentifier()).thenReturn("id");
        when(rds.getStorage()).thenReturn("storage");
        when(rds.getEngine()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> AwsResourceValidation.validateCreateRDSConfig(rds), "rds engine is missing");
    }

    @Test
    void testValidateCreateRDSConfig_MissingUsername() {
        Rds rds = mock(Rds.class);
        when(rds.getInsttype()).thenReturn("type");
        when(rds.getIdentifier()).thenReturn("id");
        when(rds.getStorage()).thenReturn("storage");
        when(rds.getEngine()).thenReturn("engine");
        when(rds.getUsername()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> AwsResourceValidation.validateCreateRDSConfig(rds), "rds username is missing");
    }

    @Test
    void testValidateCreateRDSConfig_MissingPassword() {
        Rds rds = mock(Rds.class);
        when(rds.getInsttype()).thenReturn("type");
        when(rds.getIdentifier()).thenReturn("id");
        when(rds.getStorage()).thenReturn("storage");
        when(rds.getEngine()).thenReturn("engine");
        when(rds.getUsername()).thenReturn("user");
        when(rds.getPassword()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> AwsResourceValidation.validateCreateRDSConfig(rds), "rds password is missing");
    }

    @Test
    void testValidateCreateRDSConfig_MissingName() {
        Rds rds = mock(Rds.class);
        when(rds.getInsttype()).thenReturn("type");
        when(rds.getIdentifier()).thenReturn("id");
        when(rds.getStorage()).thenReturn("storage");
        when(rds.getEngine()).thenReturn("engine");
        when(rds.getUsername()).thenReturn("user");
        when(rds.getPassword()).thenReturn("pass");
        when(rds.getName()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> AwsResourceValidation.validateCreateRDSConfig(rds), "rds name is missing");
    }

    @Test
    void testValidateCreateRDSConfig_MissingRetention() {
        Rds rds = mock(Rds.class);
        when(rds.getInsttype()).thenReturn("type");
        when(rds.getIdentifier()).thenReturn("id");
        when(rds.getStorage()).thenReturn("storage");
        when(rds.getEngine()).thenReturn("engine");
        when(rds.getUsername()).thenReturn("user");
        when(rds.getPassword()).thenReturn("pass");
        when(rds.getName()).thenReturn("name");
        when(rds.getRetention()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> AwsResourceValidation.validateCreateRDSConfig(rds), "rds retention is missing");
    }

    @Test
    void testValidateCreateRDSConfig_ValidRds() {
        Rds rds = mock(Rds.class);
        when(rds.getInsttype()).thenReturn("type");
        when(rds.getIdentifier()).thenReturn("id");
        when(rds.getStorage()).thenReturn("storage");
        when(rds.getEngine()).thenReturn("engine");
        when(rds.getUsername()).thenReturn("user");
        when(rds.getPassword()).thenReturn("pass");
        when(rds.getName()).thenReturn("name");
        when(rds.getRetention()).thenReturn("retention");

        // Assuming the mapper returns a non-null map
        when(AwsResourceConfigMapper.RdsCreateConfigMapper(rds)).thenReturn(Map.of("key", "value"));

        Map<String, String> result = AwsResourceValidation.validateCreateRDSConfig(rds);
        assertNotNull(result);
        assertEquals("value", result.get("key"));
    }

    // Tests for validateUpdateRDSConfig

    @Test
    void testValidateUpdateRDSConfig_NullRds() {
        assertNull(AwsResourceValidation.validateUpdateRDSConfig(null));
    }

    @Test
    void testValidateUpdateRDSConfig_MissingState() {
        Rds rds = mock(Rds.class);
        when(rds.getState()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> AwsResourceValidation.validateUpdateRDSConfig(rds), "rds state is missing");
    }

    @Test
    void testValidateUpdateRDSConfig_MissingDbid() {
        Rds rds = mock(Rds.class);
        when(rds.getState()).thenReturn("state");
        when(rds.getDbid()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> AwsResourceValidation.validateUpdateRDSConfig(rds), "rds id is missing");
    }

    @Test
    void testValidateUpdateRDSConfig_ValidRds() {
        Rds rds = mock(Rds.class);
        when(rds.getState()).thenReturn("state");
        when(rds.getDbid()).thenReturn("dbid");

        // Assuming the mapper returns a non-null map
        when(AwsResourceConfigMapper.RdsUpdateConfigMapper(rds)).thenReturn(Map.of("key", "value"));

        Map<String, String> result = AwsResourceValidation.validateUpdateRDSConfig(rds);
        assertNotNull(result);
        assertEquals("value", result.get("key"));
    }

}
