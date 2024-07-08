package de.cronn.liquibase.ext.postgres.model;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class EntityUsingLanguage2 {
	@Id
	private Long id;

	@JdbcType(PostgreSQLEnumJdbcType.class)
	private Language language;
}
