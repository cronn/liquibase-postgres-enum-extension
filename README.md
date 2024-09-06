[![CI](https://github.com/cronn/liquibase-postgres-enum-extension/workflows/CI/badge.svg)](https://github.com/cronn/liquibase-postgres-enum-extension/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.cronn/liquibase-postgres-enum-extension/badge.svg)](http://maven-badges.herokuapp.com/maven-central/de.cronn/liquibase-postgres-enum-extension)
[![Apache 2.0](https://img.shields.io/github/license/cronn/liquibase-postgres-enum-extension.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![codecov](https://codecov.io/gh/cronn/liquibase-postgres-enum-extension/branch/main/graph/badge.svg?token=KD1WJK5ZFK)](https://codecov.io/gh/cronn/liquibase-postgres-enum-extension)
[![Valid Gradle Wrapper](https://github.com/cronn/liquibase-postgres-enum-extension/workflows/Validate%20Gradle%20Wrapper/badge.svg)](https://github.com/cronn/liquibase-postgres-enum-extension/actions/workflows/gradle-wrapper-validation.yml)

# Liquibase Extension for native PostgreSQL enums #

This library provides support for [native PostgreSQL][postgresql-enums] enums in Liquibase.
This library was designed to be used in conjunction with our [liquibase-changelog-generator][liquibase-changelog-generator] library
such that in many cases the required changelogs are generated automatically.
Furthermore, this library was designed such that it works with Hibernate’s support for native PostgreSQL enums that was introduced in Hibernate 6.2.

## Usage ##

Add the following Maven **runtime** dependency to your project:

```xml
<dependency>
    <groupId>de.cronn</groupId>
    <artifactId>liquibase-postgres-enum-extension</artifactId>
    <version>1.0</version>
    <scope>runtime</scope>
</dependency>
```

### Hibernate

For Hibernate entities, we recommend annotating attributes of enum type as shown below:
```java
@JdbcType(PostgreSQLEnumJdbcType.class)
Status status;
```

## Commands ##

The library provides additional liquibase commands, that can be used within a `changeSet`:

### Creating an enum type

```xml
<ext:createPostgresEnumType name="color" values="RED, GREEN, BLUE"/>
```

### Adding one or more values to an existing enum type

```xml
<ext:addPostgresEnumValues enumTypeName="color" valuesToAdd="BLACK, WHITE"/>
```

### Renaming an existing enum value

```xml
<ext:renamePostgresEnumValue enumTypeName="color" oldValue="BLACK" newValue="KEY"/>
```

### Removing one or more values of an existing enum

PostgreSQL does not yet support removing of values of an existing enum. Instead, we implement a workaround
by replacing the enum with a new enum type that has different values (as described in [this blog post][yo1dog-blog]).

⚠ You need to be extra careful when you drop an enum value! First, you need to update the existing tables
to make sure that the value is not used anymore, typically using an `UPDATE` statement.

```xml
<ext:modifyPostgresEnumType name="color" newValues="CYAN, MAGENTA, YELLOW, KEY"/>
```

### Renaming an existing enum type

```xml
<ext:renamePostgresEnumType oldName="color" newName="colour"/>
```

### Dropping an existing enum type

```xml
<ext:dropPostgresEnumType name="color"/>
```

## Requirements ##

- Java 17+
- Liquibase 4.27+

[postgresql-enums]: https://www.postgresql.org/docs/current/datatype-enum.html
[liquibase-changelog-generator]: https://github.com/cronn/liquibase-changelog-generator
[yo1dog-blog]: https://blog.yo1.dog/updating-enum-values-in-postgresql-the-safe-and-easy-way/
