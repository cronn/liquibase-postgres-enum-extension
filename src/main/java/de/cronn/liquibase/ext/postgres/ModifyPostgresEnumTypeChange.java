package de.cronn.liquibase.ext.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.intellij.lang.annotations.Language;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.change.DatabaseChangeProperty;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.ValidationErrors;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawSqlStatement;

@DatabaseChange(
	name = "modifyPostgresEnumType",
	description = "Modifies the values of an existing Postgres enum type",
	priority = ChangeMetaData.PRIORITY_DEFAULT)
public class ModifyPostgresEnumTypeChange extends AbstractPostgresEnumChange {

	@Language("PostgreSQL")
	private static final String SELECT_ALL_ENUM_USAGES_QUERY = """
		SELECT c.relname AS table_name, a.attname AS column_name
		FROM pg_attribute a
		JOIN pg_class c ON a.attrelid = c.oid
		JOIN pg_namespace n ON c.relnamespace = n.oid AND n.nspname = '%s'
		WHERE a.atttypid = (SELECT oid FROM pg_type WHERE typname = '%s')
		  AND c.relkind = 'r'
		  AND NOT a.attisdropped
		ORDER BY table_name, column_name""";

	private String name;
	private List<String> newValues = new ArrayList<>();

	public ModifyPostgresEnumTypeChange() {
	}

	public ModifyPostgresEnumTypeChange(String name, List<String> newValues) {
		this.name = name;
		this.newValues = newValues;
	}

	@Override
	public ValidationErrors validate(Database database) {
		ValidationErrors validationErrors = super.validate(database);
		validationErrors.checkRequiredField("name", name);
		validationErrors.checkRequiredField("newValues", newValues);
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
		description = "The comma-separated list of new values of the enum type",
		exampleValue = "red, green, blue")
	public String getNewValues() {
		return String.join(", ", newValues);
	}

	public void setNewValues(String newValues) {
		this.newValues = splitListOfValues(newValues);
	}

	@Override
	public String getConfirmationMessage() {
		return "Modified values of enum type %s".formatted(getName());
	}

	@Override
	public boolean generateStatementsVolatile(Database database) {
		// The generated statements depend on the current database state
		return true;
	}

	@Override
	public SqlStatement[] generateStatements(Database database) {
		List<SqlStatement> statements = new ArrayList<>();

		// See https://blog.yo1.dog/updating-enum-values-in-postgresql-the-safe-and-easy-way/
		// and https://stackoverflow.com/a/47305844/4308

		String temporaryName = getName() + "_old";
		statements.add(new RawSqlStatement("alter type %s rename to %s".formatted(getName(), temporaryName)));

		CreatePostgresEnumTypeChange createPostgresEnumTypeChange = new CreatePostgresEnumTypeChange(getName(), newValues);
		statements.addAll(Arrays.asList(createPostgresEnumTypeChange.generateStatements(database)));

		JdbcConnection jdbcConnection = (JdbcConnection) database.getConnection();
		String defaultSchemaName = database.getDefaultSchemaName();
		try (Statement statement = jdbcConnection.createStatement();
			 ResultSet resultSet = statement.executeQuery(SELECT_ALL_ENUM_USAGES_QUERY.formatted(defaultSchemaName, getName()))) {
			while (resultSet.next()) {
				String tableName = resultSet.getString("table_name");
				String columnName = resultSet.getString("column_name");
				statements.add(
					new RawSqlStatement("alter table %s alter column %s type %s using %s::text::%s"
						.formatted(tableName, columnName, getName(), columnName, getName())));
			}
		} catch (SQLException | DatabaseException e) {
			throw new RuntimeException(e);
		}

		DropPostgresEnumTypeChange dropPostgresEnumTypeChange = new DropPostgresEnumTypeChange(temporaryName);
		statements.addAll(Arrays.asList(dropPostgresEnumTypeChange.generateStatements(database)));

		return statements.toArray(SqlStatement[]::new);
	}
}
