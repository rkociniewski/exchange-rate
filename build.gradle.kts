import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * artifact group
 */
group = "rk.powermilk"

/**
 * project version
 */
version = "1.0.6"

val javaVersion = JavaVersion.VERSION_21

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.test.logger)
    alias(libs.plugins.dokka)
    alias(libs.plugins.manes)
    alias(libs.plugins.detekt)
    jacoco
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

// dependencies
dependencies {
    detektPlugins(libs.detekt)
    implementation(libs.jackson.kotlin)
    testImplementation(libs.junit.params)
    testImplementation(kotlin("test"))
}

testlogger {
    showStackTraces = false
    showFullStackTraces = false
    showCauses = false
    slowThreshold = 10000
    showSimpleNames = true
}

tasks.test {
    jvmArgs("-XX:+EnableDynamicAgentLoading")
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.dokkaHtml {
    outputDirectory.set(layout.buildDirectory.dir("dokka")) // output directory of dokka documentation.
    // source set configuration.
    dokkaSourceSets {
        named("main") { // source set name.
            jdkVersion.set(java.targetCompatibility.toString().toInt()) // Used for linking to JDK documentation
            skipDeprecated.set(false) // Add output to deprecated members. PackageOptions can override applying globally
            includeNonPublic.set(true) // non-public modifiers should be documented
        }
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.75".toBigDecimal()
            }
        }

        rule {
            enabled = true
            element = "CLASS"
            includes = listOf("rk.*")

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.75".toBigDecimal()
            }
        }
    }
}

tasks.register("cleanReports") {
    doLast {
        delete("${layout.buildDirectory}/reports")
    }
}

tasks.register("coverage") {
    dependsOn(tasks.test, tasks.jacocoTestReport, tasks.jacocoTestCoverageVerification)
}

kotlin {
    compilerOptions {
        verbose = true // enable verbose logging output
        jvmTarget.set(JvmTarget.fromTarget(java.targetCompatibility.toString())) // target version of the generated JVM bytecode
    }
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

detekt {
    source.setFrom("src/main/kotlin")
    config.setFrom("$projectDir/detekt.yml")
    autoCorrect = true
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = JvmTarget.JVM_21.target
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = JvmTarget.JVM_21.target
}

private fun isNonStable(version: String): Boolean {
    return listOf("alpha", "beta", "rc", "cr", "m", "preview", "snapshot", "dev")
        .any { version.lowercase().contains(it) }
}
