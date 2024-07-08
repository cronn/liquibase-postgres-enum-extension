package de.cronn.liquibase.ext.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import de.cronn.assertions.validationfile.FileExtensions;
import de.cronn.postgres.snapshot.util.PostgresDump;
import de.cronn.postgres.snapshot.util.PostgresDumpOption;

@SpringBootTest(classes = LiquibasePostgresEnumExtensionTest.FullLiquibaseConfig.class)
@Testcontainers
class DumpWithFullLiquibaseConfigTest extends BaseTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.3");

	@Autowired
	DataSource dataSource;

	@Test
	void dumpDatabase() throws Exception {
		normalizeDatabaseChangelog();
		String dump = PostgresDump.dumpToString(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(),
			PostgresDumpOption.INSERTS);
		assertWithFile(dump, FileExtensions.SQL);
	}

	private void normalizeDatabaseChangelog() throws Exception {
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement("update databasechangelog set dateexecuted = '2024-01-02 12:00:00', deployment_id = 'test'")) {
			preparedStatement.execute();
		}
	}
}
