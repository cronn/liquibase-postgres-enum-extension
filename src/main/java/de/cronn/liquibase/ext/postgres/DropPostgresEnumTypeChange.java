package de.cronn.liquibase.ext.postgres;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.change.DatabaseChangeProperty;
import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawSqlStatement;

@DatabaseChange(name = "dropPostgresEnumType",
	description = "Drops a Postgres enum type",
	priority = ChangeMetaData.PRIORITY_DEFAULT)
public class DropPostgresEnumTypeChange extends AbstractPostgresEnumChange {

	private String name;

	public DropPostgresEnumTypeChange() {
	}

	public DropPostgresEnumTypeChange(String name) {
		this.name = name;
	}

	@Override
	public ValidationErrors validate(Database database) {
		ValidationErrors validationErrors = super.validate(database);
		validationErrors.checkRequiredField("name", name);
		return validationErrors;
	}

	public void setName(String name) {
		this.name = name;
	}

	@DatabaseChangeProperty(description = "The name of the enum type", exampleValue = "color")
	public String getName() {
		return name;
	}

	@Override
	public String getConfirmationMessage() {
		return "Dropped enum type %s".formatted(getName());
	}

	@Override
	public SqlStatement[] generateStatements(Database database) {
		return new SqlStatement[] {
			new RawSqlStatement("drop cast (varchar as %s)".formatted(name)),
			new RawSqlStatement("drop cast (%s as varchar)".formatted(name)),
			new RawSqlStatement("drop type %s".formatted(name))
		};
	}
}
