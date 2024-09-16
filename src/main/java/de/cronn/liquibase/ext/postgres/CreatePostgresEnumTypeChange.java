package de.cronn.liquibase.ext.postgres;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.change.DatabaseChangeProperty;
import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawSqlStatement;

@DatabaseChange(
	name = "createPostgresEnumType",
	description = "Creates a Postgres enum type",
	priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreatePostgresEnumTypeChange extends AbstractPostgresEnumChange {

	private String name;
	private List<String> values = new ArrayList<>();

	public CreatePostgresEnumTypeChange() {
	}

	public CreatePostgresEnumTypeChange(String name, List<String> values) {
		this.name = name;
		this.values = values;
	}

	@Override
	public ValidationErrors validate(Database database) {
		ValidationErrors validationErrors = super.validate(database);
		validationErrors.checkRequiredField("name", name);
		validationErrors.checkRequiredField("values", values);
		return validationErrors;
	}

	@DatabaseChangeProperty(
		description = "The name of the enum type",
		exampleValue = "color")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@DatabaseChangeProperty(
		description = "The comma-separated list of values of the enum type",
		exampleValue = "red, green, blue")
	public String getValues() {
		return String.join(", ", values);
	}

	public void setValues(String values) {
		this.values = splitListOfValues(values);
	}

	@Override
	public String getConfirmationMessage() {
		return "Created enum type %s".formatted(getName());
	}

	@Override
	public SqlStatement[] generateStatements(Database database) {
		String valuesForSql = values.stream().map(value -> "'" + value + "'").collect(Collectors.joining(", "));
		return new SqlStatement[] {
			new RawSqlStatement("create type %s as enum (%s)".formatted(name, valuesForSql)),
			new RawSqlStatement("create cast (varchar as %s) with inout as implicit".formatted(name)),
			new RawSqlStatement("create cast (%s as varchar) with inout as implicit".formatted(name))
		};
	}
}
