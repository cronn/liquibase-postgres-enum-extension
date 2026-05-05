buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        dependencyLocking {
            lockAllConfigurations()
        }
        components.all {
            if (id.version.matches(Regex("(?i).+([-.])(CANDIDATE|RC|BETA|ALPHA|M\\d+).*"))) {
                status = "milestone"
            }
        }
    }
}

plugins {
    id("java-library")
    id("org.springframework.boot") version "latest.release"
    id("io.spring.dependency-management") version "latest.release"
    id("jacoco")
    id("maven-publish")
    id("signing")
    id("org.jreleaser") version "latest.release"
    id("com.diffplug.spotless") version "latest.release"
}

group = "de.cronn"
version = "2.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

spotless {
    java {
        googleJavaFormat()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        ktlint()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.bootJar {
    enabled = false
}

tasks.bootRun {
    enabled = false
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:all", "-Werror"))
}

tasks.test {
    useJUnitPlatform()
    maxHeapSize = "256m"
}

dependencies {
    // Core
    implementation("org.liquibase:liquibase-core")
    compileOnly("org.jetbrains:annotations:latest.release")

    // Test: JUnit
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Test: cronn utilities
    testImplementation("de.cronn:postgres-snapshot-util:latest.release")
    testImplementation("de.cronn:liquibase-changelog-generator-postgresql:latest.release")
    testImplementation("de.cronn:test-utils:latest.release")
    testImplementation("de.cronn:validation-file-assertions:latest.release")

    // Test: AssertJ
    testImplementation("org.assertj:assertj-core")

    // Test: Spring Boot
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")

    // Test: Testcontainers
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-postgresql")

    components.all {
        if (id.version.matches(Regex("(?i).+([-.])(CANDIDATE|RC|BETA|ALPHA|M\\d+).*"))) {
            status = "milestone"
        }
    }
}

dependencyLocking {
    lockAllConfigurations()
}

tasks.wrapper {
    gradleVersion = "9.5.0"
    distributionType = Wrapper.DistributionType.ALL
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
    dependsOn(tasks.test)
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier = "sources"
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier = "javadoc"
    from(tasks.javadoc.get().destinationDir)
    dependsOn(tasks.javadoc)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            pom {
                name = project.name
                description = "Liquibase extension for native PostgreSQL enums"
                url = "https://github.com/cronn/liquibase-postgres-enum-extension"

                licenses {
                    license {
                        name = "The Apache Software License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                        distribution = "repo"
                    }
                }

                developers {
                    developer {
                        id = "benedikt.waldvogel"
                        name = "Benedikt Waldvogel"
                        email = "benedikt.waldvogel@cronn.de"
                    }
                }

                scm {
                    url = "https://github.com/cronn/liquibase-postgres-enum-extension"
                }
            }

            from(components["java"])

            artifact(sourcesJar)
            artifact(javadocJar)

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }
    repositories {
        maven {
            name = "staging"
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}

jreleaser {
    signing {
        active = org.jreleaser.model.Active.NEVER
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active = org.jreleaser.model.Active.RELEASE
                    sign = false
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}
