package de.cronn.liquibase.ext.postgres.diff;

import de.cronn.liquibase.ext.postgres.DropPostgresEnumTypeChange;
import liquibase.change.Change;
import liquibase.database.Database;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.ChangeGeneratorChain;
import liquibase.diff.output.changelog.UnexpectedObjectChangeGenerator;
import liquibase.structure.DatabaseObject;

public class UnexpectedPostgresEnumTypeChangeGenerator
	extends AbstractPostgresEnumTypeChangeGenerator implements UnexpectedObjectChangeGenerator {

	@Override
	public Change[] fixUnexpected(
		DatabaseObject unexpectedObject,
		DiffOutputControl control,
		Database referenceDatabase,
		Database comparisonDatabase,
		ChangeGeneratorChain chain) {
		return new Change[] { new DropPostgresEnumTypeChange(unexpectedObject.getName()) };
	}
}
