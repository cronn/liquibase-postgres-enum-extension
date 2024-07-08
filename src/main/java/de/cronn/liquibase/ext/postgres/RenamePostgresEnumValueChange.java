package de.cronn.liquibase.ext.postgres;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.change.DatabaseChangeProperty;
import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawCallStatement;

@DatabaseChange(name = "renamePostgresEnumValue",
	description = "Renames an enum value of an existing Postgres enum type",
	priority = ChangeMetaData.PRIORITY_DEFAULT)
public class RenamePostgresEnumValueChange extends AbstractPostgresEnumChange {

	private String enumTypeName;
	private String oldValue;
	private String newValue;

	@Override
	public ValidationErrors validate(Database database) {
		ValidationErrors validationErrors = super.validate(database);
		validationErrors.checkRequiredField("enumTypeName", enumTypeName);
		validationErrors.checkRequiredField("oldValue", oldValue);
		validationErrors.checkRequiredField("newValue", newValue);
		return validationErrors;
	}

	@DatabaseChangeProperty(description = "The name of the enum type", exampleValue = "color")
	public String getEnumTypeName() {
		return enumTypeName;
	}

	public void setEnumTypeName(String enumTypeName) {
		this.enumTypeName = enumTypeName;
	}

	@DatabaseChangeProperty(description = "The old value of the enum type", exampleValue = "REED")
	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	@DatabaseChangeProperty(description = "The new value of the enum type", exampleValue = "RED")
	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	@Override
	public String getConfirmationMessage() {
		return "Renamed value of enum type %s from %s to %s".formatted(getEnumTypeName(), getOldValue(), getNewValue());
	}

	@Override
	public SqlStatement[] generateStatements(Database database) {
		return new SqlStatement[] {
			new RawCallStatement("alter type %s rename value '%s' to '%s'".formatted(enumTypeName, oldValue, newValue))
		};
	}
}
