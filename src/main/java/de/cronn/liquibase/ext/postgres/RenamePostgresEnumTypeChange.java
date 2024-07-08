package de.cronn.liquibase.ext.postgres;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.change.DatabaseChangeProperty;
import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawCallStatement;

@DatabaseChange(name = "renamePostgresEnumType", description = "Renames a Postgres enum type", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class RenamePostgresEnumTypeChange extends AbstractPostgresEnumChange {

	private String oldName;
	private String newName;

	@Override
	public ValidationErrors validate(Database database) {
		ValidationErrors validationErrors = super.validate(database);
		validationErrors.checkRequiredField("oldName", oldName);
		validationErrors.checkRequiredField("newName", newName);
		return validationErrors;
	}

	@DatabaseChangeProperty(description = "The old name of the enum type", exampleValue = "colour")
	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	@DatabaseChangeProperty(description = "The new name of the enum type", exampleValue = "color")
	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	@Override
	public String getConfirmationMessage() {
		return "Renamed enum type from %s to %s".formatted(getOldName(), getNewName());
	}

	@Override
	public SqlStatement[] generateStatements(Database database) {
		return new SqlStatement[] { new RawCallStatement("alter type %s rename to %s".formatted(oldName, newName)) };
	}
}
