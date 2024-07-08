package de.cronn.liquibase.ext.postgres.diff;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import org.intellij.lang.annotations.Language;

import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.jvm.JdbcSnapshotGenerator;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

public class PostgresEnumTypeSnapshotGenerator extends JdbcSnapshotGenerator {

	@Language("PostgreSQL")
	private static final String COLLECT_ENUMS_QUERY = """
		select t.typname as name, array_agg(distinct e.enumlabel) as values
		from pg_enum e
		join pg_type t on t.oid = e.enumtypid
		join pg_namespace ns on t.typnamespace = ns.oid and ns.nspname = '%s'
		group by t.typname
		order by t.typname""";

	public PostgresEnumTypeSnapshotGenerator() {
		super(PostgresEnumType.class, new Class[] { Schema.class });
	}

	@Override
	protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot) {
		return example;
	}

	@Override
	protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot) throws DatabaseException {
		if (!snapshot.getSnapshotControl().shouldInclude(PostgresEnumType.class)) {
			return;
		}
		if (snapshot.getDatabase().getConnection() instanceof JdbcConnection jdbcConnection) {
			try {
				Schema schema = foundObject.getSchema();
				String schemaName = schema.getName();
				ResultSet resultSet = jdbcConnection.createStatement().executeQuery(COLLECT_ENUMS_QUERY.formatted(schemaName));

				while (resultSet.next()) {
					String typeName = resultSet.getString("name");
					List<String> values = collectValues(resultSet.getArray("values"));
					PostgresEnumType postgresEnumType = new PostgresEnumType();
					postgresEnumType.setSchema(schema);
					postgresEnumType.setName(typeName);
					postgresEnumType.setValues(values);
					schema.addDatabaseObject(postgresEnumType);
				}
			} catch (SQLException e) {
				throw new DatabaseException(e);
			}
		}
	}

	private static List<String> collectValues(Array array) throws SQLException {
		String[] values = (String[]) array.getArray();
		return Stream.of(values)
			.sorted()
			.toList();
	}
}
