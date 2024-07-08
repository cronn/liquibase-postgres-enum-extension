package de.cronn.liquibase.ext.postgres.diff;

import java.util.List;

import de.cronn.liquibase.ext.postgres.CreatePostgresEnumTypeChange;
import liquibase.change.Change;
import liquibase.database.Database;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.ChangeGeneratorChain;
import liquibase.diff.output.changelog.MissingObjectChangeGenerator;
import liquibase.structure.DatabaseObject;

public class MissingPostgresEnumTypeChangeGenerator extends AbstractPostgresEnumTypeChangeGenerator
	implements MissingObjectChangeGenerator {

	@Override
	public Change[] fixMissing(
		DatabaseObject missingObject,
		DiffOutputControl control,
		Database referenceDatabase,
		Database comparisonDatabase,
		ChangeGeneratorChain chain) {
		String name = missingObject.getName();
		List<String> values = missingObject.getAttribute("values", List.class);
		return new Change[] { new CreatePostgresEnumTypeChange(name, values) };
	}
}
