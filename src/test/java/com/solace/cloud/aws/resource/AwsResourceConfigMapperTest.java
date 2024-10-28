package com.solace.cloud.aws.resource;

import com.solace.configHandler.aws.Ec2;
import com.solace.configHandler.aws.Rds;
import com.solace.configHandler.aws.Subnet;
import com.solace.configHandler.aws.Vpc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

public class AwsResourceConfigMapperTest {

    @Test
    void testVpcCreateConfigMapper() {
        // Arrange
        Vpc vpc = mock(Vpc.class);
        when(vpc.getCidrblock()).thenReturn("10.0.0.0/16");

        Subnet subnet = mock(Subnet.class);
        when(subnet.getCidrblock()).thenReturn("10.0.1.0/24");
        when(subnet.getAvzone()).thenReturn("us-east-1a");

        String secGroup = "sg-12345";

        // Act
        Map<String, String> result = AwsResourceConfigMapper.VpcCreateConfigMapper(vpc, subnet, secGroup);

        // Assert
        assertNotNull(result);
        assertEquals("10.0.0.0/16", result.get("vpc_cidr"));
        assertEquals("10.0.1.0/24", result.get("subnet_cidr"));
        assertEquals("us-east-1a", result.get("az_region"));
        assertEquals(secGroup, result.get("security_group"));
    }

    @Test
    void testEc2CreateConfigMapper() {
        // Arrange
        Ec2 ec2 = mock(Ec2.class);
        when(ec2.getInsttype()).thenReturn("t2.micro");
        when(ec2.getAmi()).thenReturn("ami-123456");

        // Act
        Map<String, String> result = AwsResourceConfigMapper.Ec2CreateConfigMapper(ec2);

        // Assert
        assertNotNull(result);
        assertEquals("t2.micro", result.get("instanceType"));
        assertEquals("ami-123456", result.get("ami"));
    }

    @Test
    void testEc2UpdateConfigMapper() {
        // Arrange
        Ec2 ec2 = mock(Ec2.class);
        when(ec2.getState()).thenReturn("running");
        when(ec2.getId()).thenReturn("i-123456");

        // Act
        Map<String, String> result = AwsResourceConfigMapper.Ec2UpdateConfigMapper(ec2);

        // Assert
        assertNotNull(result);
        assertEquals("running", result.get("state"));
        assertEquals("i-123456", result.get("id"));
    }

    @Test
    void testRdsCreateConfigMapper() {
        // Arrange
        Rds rds = mock(Rds.class);
        when(rds.getInsttype()).thenReturn("db.t2.micro");
        when(rds.getIdentifier()).thenReturn("mydb");
        when(rds.getStorage()).thenReturn("20");
        when(rds.getEngine()).thenReturn("mysql");
        when(rds.getUsername()).thenReturn("admin");
        when(rds.getPassword()).thenReturn("password");
        when(rds.getName()).thenReturn("mydatabase");
        when(rds.getRetention()).thenReturn("7");

        // Act
        Map<String, String> result = AwsResourceConfigMapper.RdsCreateConfigMapper(rds);

        // Assert
        assertNotNull(result);
        assertEquals("db.t2.micro", result.get("instanceType"));
        assertEquals("mydb", result.get("identifier"));
        assertEquals("20", result.get("storage"));
        assertEquals("mysql", result.get("engine"));
        assertEquals("admin", result.get("username"));
        assertEquals("password", result.get("password"));
        assertEquals("mydatabase", result.get("name"));
        assertEquals("7", result.get("retention"));
    }

    @Test
    void testRdsUpdateConfigMapper() {
        // Arrange
        Rds rds = mock(Rds.class);
        when(rds.getState()).thenReturn("available");
        when(rds.getDbid()).thenReturn("db-123456");

        // Act
        Map<String, String> result = AwsResourceConfigMapper.RdsUpdateConfigMapper(rds);

        // Assert
        assertNotNull(result);
        assertEquals("available", result.get("state"));
        assertEquals("db-123456", result.get("id"));
    }

}
