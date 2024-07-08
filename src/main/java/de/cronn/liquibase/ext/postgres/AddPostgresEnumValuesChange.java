package de.cronn.liquibase.ext.postgres;

import java.util.ArrayList;
import java.util.List;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.change.DatabaseChangeProperty;
import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawCallStatement;

@DatabaseChange(name = "addPostgresEnumValues",
	description = "Adds enum values to an existing Postgres enum type",
	priority = ChangeMetaData.PRIORITY_DEFAULT)
public class AddPostgresEnumValuesChange extends AbstractPostgresEnumChange {

	private String enumTypeName;
	private List<String> valuesToAdd;

	public AddPostgresEnumValuesChange() {
	}

	public AddPostgresEnumValuesChange(String enumTypeName, List<String> valuesToAdd) {
		this.enumTypeName = enumTypeName;
		this.valuesToAdd = valuesToAdd;
	}

	@Override
	public ValidationErrors validate(Database database) {
		ValidationErrors validationErrors = super.validate(database);
		validationErrors.checkRequiredField("enumTypeName", enumTypeName);
		validationErrors.checkRequiredField("valuesToAdd", valuesToAdd);
		return validationErrors;
	}

	@DatabaseChangeProperty(description = "The name of the enum type", exampleValue = "color")
	public String getEnumTypeName() {
		return enumTypeName;
	}

	public void setEnumTypeName(String enumTypeName) {
		this.enumTypeName = enumTypeName;
	}

	@DatabaseChangeProperty(description = "The comma-separated list of new values of the enum type",
		exampleValue = "RED, GREEN")
	public String getValuesToAdd() {
		return String.join(", ", valuesToAdd);
	}

	public void setValuesToAdd(String valuesToAdd) {
		this.valuesToAdd = splitListOfValues(valuesToAdd);
	}

	@Override
	public String getConfirmationMessage() {
		return "Added value %s to enum %s".formatted(getValuesToAdd(), getEnumTypeName());
	}

	@Override
	public SqlStatement[] generateStatements(Database database) {
		List<SqlStatement> statements = new ArrayList<>();
		for (String valueToAdd : valuesToAdd) {
			statements.add(new RawCallStatement("alter type %s add value '%s'".formatted(enumTypeName, valueToAdd)));
		}
		return statements.toArray(SqlStatement[]::new);
	}
}
