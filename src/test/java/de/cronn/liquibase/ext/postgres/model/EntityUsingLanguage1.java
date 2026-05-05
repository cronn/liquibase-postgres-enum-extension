package de.cronn.liquibase.ext.postgres.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

@Entity
public class EntityUsingLanguage1 {
  @Id private Long id;

  @JdbcType(PostgreSQLEnumJdbcType.class)
  private Language language;
}
