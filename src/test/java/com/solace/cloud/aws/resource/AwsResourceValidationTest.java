package com.solace.cloud.aws.resource;

import com.solace.configHandler.aws.Ec2;
import com.solace.configHandler.aws.Rds;
import com.solace.configHandler.aws.Subnet;
import com.solace.configHandler.aws.Vpc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

public class AwsResourceValidationTest {

    private static final String REGION = "us-east-1";

    @Test
    void testValidateCreateVPCConfig_NullVPC() {
        assertNull(AwsResourceValidation.validateCreateVPCConfig(null, REGION), "Expected result to be null for null VPC config.");
    }

    @Test
    void testValidateCreateVPCConfig_MissingCidrBlock() {
        Vpc vpc = mock(Vpc.class);
        when(vpc.getCidrblock()).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                AwsResourceValidation.validateCreateVPCConfig(vpc, REGION)
        );
        assertEquals("Invalid or missing VPC CIDR block: null", exception.getMessage());
    }

    @Test
    void testValidateCreateVPCConfig_InvalidCidrBlock() {
        Vpc vpc = mock(Vpc.class);
        when(vpc.getCidrblock()).thenReturn("invalid-cidr");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                AwsResourceValidation.validateCreateVPCConfig(vpc, REGION)
        );
        assertEquals("Invalid or missing VPC CIDR block: invalid-cidr", exception.getMessage());
    }

    @Test
    void testValidateCreateVPCConfig_MissingSubnet() {
        Vpc vpc = mock(Vpc.class);
        when(vpc.getCidrblock()).thenReturn("192.168.1.0/24");
        when(vpc.getSubnet()).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                AwsResourceValidation.validateCreateVPCConfig(vpc, REGION)
        );
        assertEquals("Invalid or missing subnet", exception.getMessage());
    }

    @Test
    void testValidateCreateVPCConfig_MissingSubnetCidrBlock() {
        Vpc vpc = mock(Vpc.class);
        when(vpc.getCidrblock()).thenReturn("192.168.1.0/24");

        Subnet subnet = mock(Subnet.class);
        when(vpc.getSubnet()).thenReturn(subnet);
        when(subnet.getCidrblock()).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                AwsResourceValidation.validateCreateVPCConfig(vpc, REGION)
        );
        assertEquals("Invalid or missing Public Subnet CIDR block: null", exception.getMessage());
    }

    @Test
    void testValidateCreateEC2Config_NullEc2() {
        assertNull(AwsResourceValidation.validateCreateEC2Config(null), "Expected result to be null for null EC2 config.");
    }

    @Test
    void testValidateCreateEC2Config_MissingInstType() {
        Ec2 ec2 = mock(Ec2.class);
        when(ec2.getInsttype()).thenReturn(null);
        when(ec2.getAmi()).thenReturn("ami-12345678"); // Set other fields as needed

        assertThrows(IllegalArgumentException.class, () ->
                AwsResourceValidation.validateCreateEC2Config(ec2), "ec2 Instance type is missing");
    }

    @Test
    void testValidateCreateEC2Config_MissingAmi() {
        Ec2 ec2 = mock(Ec2.class);
        when(ec2.getInsttype()).thenReturn("t2.micro");
        when(ec2.getAmi()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                AwsResourceValidation.validateCreateEC2Config(ec2), "ec2 ami is missing");
    }

    @Test
    void testValidateCreateEC2Config_ValidConfig() {
        Ec2 ec2 = mock(Ec2.class);
        when(ec2.getInsttype()).thenReturn("t2.micro");
        when(ec2.getAmi()).thenReturn("ami-12345678");

        Map<String, String> result = AwsResourceValidation.validateCreateEC2Config(ec2);
        assertNotNull(result, "Expected result to be not null for valid EC2 config.");
        // Additional assertions can be added based on the mapper output
    }

    @Test
    void testValidateUpdateEC2Config_NullEc2() {
        assertNull(AwsResourceValidation.validateUpdateEC2Config(null), "Expected result to be null for null EC2 config.");
    }

    @Test
    void testValidateUpdateEC2Config_MissingState() {
        Ec2 ec2 = mock(Ec2.class);
        when(ec2.getState()).thenReturn(null);
        when(ec2.getId()).thenReturn("i-1234567890abcdef0"); // Set other fields as needed

        assertThrows(IllegalArgumentException.class, () ->
                AwsResourceValidation.validateUpdateEC2Config(ec2), "ec2 state is missing");
    }

    @Test
    void testValidateUpdateEC2Config_MissingId() {
        Ec2 ec2 = mock(Ec2.class);
        when(ec2.getState()).thenReturn("running");
        when(ec2.getId()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                AwsResourceValidation.validateUpdateEC2Config(ec2), "ec2 id is missing");
    }

    @Test
    void testValidateUpdateEC2Config_ValidConfig() {
        Ec2 ec2 = mock(Ec2.class);
        when(ec2.getState()).thenReturn("running");
        when(ec2.getId()).thenReturn("i-1234567890abcdef0");

        Map<String, String> result = AwsResourceValidation.validateUpdateEC2Config(ec2);
        assertNotNull(result, "Expected result to be not null for valid EC2 config.");
        // Additional assertions can be added based on the mapper output
    }

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

        // Act
        Map<String, String> result = AwsResourceValidation.validateCreateRDSConfig(rds);

        // Assert
        assertNotNull(result);
        assertEquals("type", result.get("instanceType"));
        assertEquals("id", result.get("identifier"));
        assertEquals("storage", result.get("storage"));
        assertEquals("engine", result.get("engine"));
        assertEquals("user", result.get("username"));
        assertEquals("pass", result.get("password"));
        assertEquals("name", result.get("name"));
        assertEquals("retention", result.get("retention"));
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
        when(rds.getState()).thenReturn("art");
        when(rds.getDbid()).thenReturn("1234");

        Map<String, String> result = AwsResourceValidation.validateUpdateRDSConfig(rds);
        assertNotNull(result);
        assertEquals("art", result.get("state"));
        assertEquals("1234", result.get("id"));
    }

}
