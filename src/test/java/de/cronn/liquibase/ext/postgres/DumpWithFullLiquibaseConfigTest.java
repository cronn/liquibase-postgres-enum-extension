package de.cronn.liquibase.ext.postgres;

import de.cronn.assertions.validationfile.FileExtensions;
import de.cronn.assertions.validationfile.normalization.IdNormalizer;
import de.cronn.assertions.validationfile.normalization.IncrementingIdProvider;
import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;
import de.cronn.postgres.snapshot.util.PostgresDump;
import de.cronn.postgres.snapshot.util.PostgresDumpOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest(classes = LiquibasePostgresEnumExtensionTest.FullLiquibaseConfig.class)
@Testcontainers
class DumpWithFullLiquibaseConfigTest extends BaseTest {

  @Container @ServiceConnection
  static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.1");

  @Autowired DataSource dataSource;

  @Test
  void dumpDatabase() throws Exception {
    normalizeDatabaseChangelog();
    String dump =
        PostgresDump.dumpToString(
            postgres.getJdbcUrl(),
            postgres.getUsername(),
            postgres.getPassword(),
            PostgresDumpOption.INSERTS);
    assertWithFile(dump, maskRestrict(), FileExtensions.SQL);
  }

  private ValidationNormalizer maskRestrict() {
    return new IdNormalizer(
        new IncrementingIdProvider(), "RESTRICT_", "\\\\(?:un)?restrict ([a-zA-Z0-9]{63})");
  }

  private void normalizeDatabaseChangelog() throws Exception {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement =
            connection.prepareStatement(
                "update databasechangelog set dateexecuted = '2024-01-02 12:00:00', deployment_id = 'test'")) {
      preparedStatement.execute();
    }
  }
}
