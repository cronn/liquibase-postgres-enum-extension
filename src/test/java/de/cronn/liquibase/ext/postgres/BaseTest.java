package de.cronn.liquibase.ext.postgres;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import de.cronn.assertions.validationfile.junit5.JUnit5ValidationFileAssertions;

@ExtendWith(SoftAssertionsExtension.class)
public abstract class BaseTest implements JUnit5ValidationFileAssertions {
	@InjectSoftAssertions
	private SoftAssertions softly;

	@Override
	public FailedAssertionHandler failedAssertionHandler() {
		return callable -> softly.check(callable::call);
	}

}
