import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.dokka)
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")

    tasks.withType<DokkaTaskPartial>().configureEach {
        dokkaSourceSets.configureEach {
            outputDirectory.set(file("$rootDir/docs"))
            reportUndocumented.set(false)
            suppressInheritedMembers.set(true)
            suppressObviousFunctions.set(true)
            documentedVisibilities.set(setOf(Visibility.PUBLIC))
        }
    }
}
