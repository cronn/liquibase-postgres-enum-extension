package de.cronn.liquibase.ext.postgres.diff;

import java.util.List;

import de.cronn.liquibase.ext.postgres.AddPostgresEnumValuesChange;
import de.cronn.liquibase.ext.postgres.ModifyPostgresEnumTypeChange;
import liquibase.change.Change;
import liquibase.database.Database;
import liquibase.diff.Difference;
import liquibase.diff.ObjectDifferences;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.ChangeGeneratorChain;
import liquibase.diff.output.changelog.ChangedObjectChangeGenerator;
import liquibase.structure.DatabaseObject;

public class ChangedPostgresEnumTypeChangeGenerator extends AbstractPostgresEnumTypeChangeGenerator
	implements ChangedObjectChangeGenerator {


	@Override
	public Change[] fixChanged(DatabaseObject changedObject, ObjectDifferences differences, DiffOutputControl control, Database referenceDatabase, Database comparisonDatabase, ChangeGeneratorChain chain) {
		Difference values = differences.getDifference("values");
		@SuppressWarnings("unchecked")
		List<String> referenceValues = (List<String>) values.getReferenceValue();
		@SuppressWarnings("unchecked")
		List<String> comparedValues = (List<String>) values.getComparedValue();

		List<String> removedValues = comparedValues.stream()
			.filter(value -> !referenceValues.contains(value))
			.toList();

		List<String> addedValues = referenceValues.stream()
			.filter(value -> !comparedValues.contains(value))
			.toList();

		if (!removedValues.isEmpty()) {
			return new Change[] {
				new ModifyPostgresEnumTypeChange(changedObject.getName(), referenceValues)
			};
		} else {
			return new Change[] {
				new AddPostgresEnumValuesChange(changedObject.getName(), addedValues)
			};
		}
	}

}
