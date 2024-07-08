package de.cronn.liquibase.ext.postgres;

import liquibase.change.AbstractChange;
import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;

import java.util.Arrays;
import java.util.List;

abstract class AbstractPostgresEnumChange extends AbstractChange {

    protected static List<String> splitListOfValues(String valuesToAdd) {
        return Arrays.stream(valuesToAdd.split(",")).map(String::trim).toList();
    }

    @Override
	public boolean supports(Database database) {
		return database instanceof PostgresDatabase && super.supports(database);
	}

}
