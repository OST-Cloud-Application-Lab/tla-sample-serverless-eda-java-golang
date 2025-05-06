package org.contextmapper.sample.tlas;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

class OnionArchitectureTest {

    private static final String BASE_PACKAGE = "org.contextmapper.sample.tlas";

    /**
     * Ensures that our code complies with the Onion Architecture.
     * Checkout our ADR for more information:
     * https://github.com/ContextMapper/ddd-cm-tla-sample-application/blob/master/docs/madr/0001-onion-architecture.md
     */
    @Test
    void codeShouldAdhereToOnionArchitecture() {
        onionArchitecture()
                .domainModels(BASE_PACKAGE + ".domain..")
                .applicationServices(BASE_PACKAGE + ".application..")
                .adapter("web-api", BASE_PACKAGE + ".infrastructure.webapi..")
                .adapter("persistence", BASE_PACKAGE + ".infrastructure.persistence..")
                .adapter("application", BASE_PACKAGE + ".infrastructure.application..")
                .adapter("migration", BASE_PACKAGE + ".infrastructure.migration..")
                .ensureAllClassesAreContainedInArchitecture()
                .withOptionalLayers(true) // no domain services yet
                .check(new ClassFileImporter()
                        .withImportOption(DO_NOT_INCLUDE_TESTS)
                        .importPackages(BASE_PACKAGE));
    }

}
