package de.cronn.liquibase.ext.postgres.diff;

import liquibase.database.Database;
import liquibase.diff.output.changelog.AbstractChangeGenerator;
import liquibase.structure.DatabaseObject;

public abstract class AbstractPostgresEnumTypeChangeGenerator extends AbstractChangeGenerator {
	@Override
	public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
		if (PostgresEnumType.class.isAssignableFrom(objectType)) {
			return PRIORITY_DEFAULT;
		}
		return PRIORITY_NONE;
	}

	@Override
	public Class<? extends DatabaseObject>[] runAfterTypes() {
		return new Class[0];
	}

	@Override
	public Class<? extends DatabaseObject>[] runBeforeTypes() {
		return new Class[0];
	}
}
