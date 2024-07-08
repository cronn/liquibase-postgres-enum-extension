package de.cronn.liquibase.ext.postgres;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;

import de.cronn.assertions.validationfile.FileExtensions;
import de.cronn.assertions.validationfile.normalization.SimpleRegexReplacement;
import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;
import de.cronn.liquibase.changelog.generator.postgresql.HibernatePopulatedConfigForPostgres;
import de.cronn.liquibase.changelog.generator.postgresql.HibernateToLiquibaseDiffForPostgres;
import de.cronn.liquibase.changelog.generator.postgresql.LiquibasePopulatedConfigForPostgres;

class LiquibasePostgresEnumExtensionTest extends BaseTest {

	private final HibernateToLiquibaseDiffForPostgres differ = new HibernateToLiquibaseDiffForPostgres("Jane Doe");

	@Test
	void testGenerateDiff_emptyLiquibaseChangelog() {
		String diff = differ.generateDiff(HibernatePopulatedTestConfig.class, EmptyLiquibaseConfig.class);
		assertWithFile(diff, maskGeneratedId(), FileExtensions.XML);
	}

	@Test
	void testGenerateDiff_fullLiquibaseChangelog() {
		String diff = differ.generateDiff(HibernatePopulatedTestConfig.class, FullLiquibaseConfig.class);
		assertWithFile(diff, maskGeneratedId(), FileExtensions.XML);
	}

	private static ValidationNormalizer maskGeneratedId() {
		return new SimpleRegexReplacement("id=\"\\d{10,}-", "id=\"[MASKED]-");
	}

	@PropertySource("classpath:/empty-changelog.properties")
	static class EmptyLiquibaseConfig extends LiquibasePopulatedConfigForPostgres {
	}

	@PropertySource("classpath:/full-changelog.properties")
	static class FullLiquibaseConfig extends LiquibasePopulatedConfigForPostgres {
	}

	@EntityScan("de.cronn.liquibase.ext.postgres.model")
	static class HibernatePopulatedTestConfig extends HibernatePopulatedConfigForPostgres {
	}
}
