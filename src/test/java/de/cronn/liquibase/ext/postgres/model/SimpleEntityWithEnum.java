package de.cronn.liquibase.ext.postgres.model;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SimpleEntityWithEnum {
	@Id
	private Long id;

	@JdbcType(PostgreSQLEnumJdbcType.class)
	private Color color;

}
