package org.contextmapper.sample.tlas.domain;

import org.contextmapper.archunit.AbstractTacticArchUnitTest;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.properties.HasType.Predicates.rawType;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * This ArchUnit test uses our Context Mapper Extension to verify whether the code is in sync with the
 * Context Mapper domain model in TLAs-context.cml.
 * <p>
 * ArchUnit extension documentation: https://github.com/ContextMapper/context-mapper-archunit-extension
 */
class TacticDDDModelTest extends AbstractTacticArchUnitTest {

    @Override
    protected String getCMLFilePath() {
        return "src/main/cml/TLAs-context.cml";
    }

    @Override
    protected String getBoundedContextName() {
        return "TLA_Resolver";
    }

    @Override
    protected String getJavaPackageName2Test() {
        return "org.contextmapper.sample.tlas.domain";
    }

    @Test
    void domainModelClassesShouldBeAnnotatedWithJMolecules() {
        classes().that()
                .areNotEnums()
                .and().areNotAssignableTo(Exception.class)
                .and().areNotNestedClasses()
                .should().beAnnotatedWith(rawType(resideInAPackage("org.jmolecules..")))
                .check(classes);
    }

}
