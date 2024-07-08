package de.cronn.liquibase.ext.postgres.diff;

import java.util.List;

import liquibase.structure.AbstractDatabaseObject;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

public class PostgresEnumType extends AbstractDatabaseObject {

	@Override
	public DatabaseObject[] getContainingObjects() {
		return null;
	}

	@Override
	public String getName() {
		return this.getAttribute("name", String.class);
	}

	@Override
	public DatabaseObject setName(String name) {
		this.setAttribute("name", name);
		return this;
	}

	public void setSchema(Schema schema) {
		this.setAttribute("schema", schema);
	}

	@Override
	public Schema getSchema() {
		return this.getAttribute("schema", Schema.class);
	}

	void setValues(List<String> values) {
		this.setAttribute("values", values);
	}
}
