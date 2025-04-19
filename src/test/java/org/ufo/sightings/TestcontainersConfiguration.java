package org.ufo.sightings;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	MySQLContainer<?> mysqlContainer() {
		return new MySQLContainer<>(DockerImageName.parse("mysql:8"))
				.withDatabaseName("testdb")
				.withUsername("testuser")
				.withPassword("testpass")
				.withInitScript("schema.sql");
	}
	//Making bean static will persist across JVM for duration of tests
}
