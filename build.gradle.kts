import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.internal.logging.text.StyledTextOutput
import org.gradle.internal.logging.text.StyledTextOutputFactory
import org.gradle.kotlin.dsl.support.serviceOf
import java.io.ByteArrayOutputStream
import java.net.URL

/**
 * artifact group
 */
group = "org.powermilk"

/**
 * project version
 */
version = "1.2.0"

/**
 * project description
 */
description = "Application shows minimal and maximal value for exchange rate of GBP against EUR from last 10 days"

/**
 * Java source compatibility
 */
java.sourceCompatibility = JavaVersion.VERSION_21

/**
 * Java target compatibility
 */
java.targetCompatibility = JavaVersion.VERSION_21

/**
 * detekt plugin version
 */
val detektVersion: String by project

/**
 * dokka plugin version
 */
val dokkaVersion: String by project

/**
 * gson dependency version
 */
val gsonVersion: String by project

/**
 * junit version
 */
val junitJupiterVersion: String by project

/**
 * kotlin version
 */
val kotlinVersion: String by project

/**
 * console output style
 */
val out: StyledTextOutput = project.serviceOf<StyledTextOutputFactory>().create("an-output")

/**
 * working profile
 */
val profile = setOf("test", "dev", "prod").find { it == project.properties["profile"] } ?: "dev"

/**
 * working branch
 */
val workingGitBranch = ByteArrayOutputStream().use {
    exec {
        commandLine = "git rev-parse --abbrev-ref HEAD".split(" ")
        standardOutput = it
    }
    it.toString().trim()
}

// dependency repositories
repositories {
    google()
    gradlePluginPortal()
    maven("https://plugins.gradle.org/m2/")
    mavenLocal()
    mavenCentral()
}

// plugins
plugins {
    id("com.adarshr.test-logger")
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.dokka")
    kotlin("jvm")
}

// dependencies
dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

// source sets for build
sourceSets {
    main {
        java.srcDir("src/main/kotlin")
        resources.srcDir("src/main/resources")
    }
}

// detekt configuration
detekt {
    source.setFrom("src/main/kotlin")
    config.setFrom("$projectDir/detekt.yml")
    autoCorrect = true
}

// test logger configuration
testlogger {
    /**
     * logger theme
     */
    theme = ThemeType.MOCHA_PARALLEL
    /**
     * Should show exceptions?
     */
    showExceptions = true
    /**
     * should show stack trace?
     */
    showStackTraces = false
    /**
     * should show full stack trace?
     */
    showFullStackTraces = false
    /**
     * should show exception cause?
     */
    showCauses = false
    @Suppress("MagicNumber")
    /**
     * slow test threshold (in milliseconds)
     */
    slowThreshold = 10000
    /**
     * should show summary?
     */
    showSummary = true
    /**
     * should show simple names?
     */
    showSimpleNames = true
    /**
     * should show pass tests?
     */
    showPassed = true
    /**
     * should show skipped tests?
     */
    showSkipped = true
    /**
     * should show failed tests?
     */
    showFailed = true
    /**
     * show summary in standard stream?
     */
    showStandardStreams = false
    /**
     * show passed tests in standard stream?
     */
    showPassedStandardStreams = true
    /**
     * show skipped tests in standard stream?
     */
    showSkippedStandardStreams = true
    /**
     * show failed tests in standard stream?
     */
    showFailedStandardStreams = true
}

// build tasks
tasks {
    // display working profile
    out.info("Building with profile: $profile")
    // display working branch
    out.info("On branch: $workingGitBranch")

    // compile kotlin configuration
    compileKotlin {
        kotlinOptions {
            /**
             * report an error if there are any warnings
             */
            allWarningsAsErrors = true
            /**
             * enable verbose logging output
             */
            verbose = true
            /**
             * target version of the generated JVM bytecode
             */
            jvmTarget = java.targetCompatibility.toString()
            /**
             * A list of additional compiler arguments
             */
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    // compile kotlin tests configuration
    compileTestKotlin {
        kotlinOptions {
            /**
             * enable verbose logging output
             */
            verbose = true
            /**
             * target version of the generated JVM bytecode
             */
            jvmTarget = java.targetCompatibility.toString()
            /**
             * A list of additional compiler arguments
             */
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    detekt.configure {
        reports {
            jvmTarget = java.targetCompatibility.toString()
            xml.required.set(false)
            txt.required.set(false)
            sarif.required.set(false)

            html.required.set(true)
            html.outputLocation.set(file("${layout.buildDirectory}/reports/detekt/detekt.html"))

        }
    }

    //dokka configuration
    dokkaHtml {
        /**
         * output directory of dokka documentation.
         */
        outputDirectory.set(layout.buildDirectory.dir("dokka"))
        /**
         * source set configuration.
         */
        dokkaSourceSets {
            /**
             * source set name.
             */
            named("main") {
                @Suppress("MagicNumber")
                /**
                 * Used for linking to JDK documentation
                 */
                jdkVersion.set(java.targetCompatibility.toString().toInt())
                /**
                 * Do not output deprecated members. Applies globally, can be overridden by packageOptions
                 */
                skipDeprecated.set(false)

                /**
                 * non-public modifiers should be documented
                 */
                includeNonPublic.set(true)

            }
        }
    }

    // test run configuration
    test {
        useJUnitPlatform()
    }
}


/**
 * Function extension to create URL from [String]
 */
fun url(path: String): URL = uri(path).toURL()

/**
 * Function extension to log output in specified style.
 */
fun StyledTextOutput.color(any: Any, style: StyledTextOutput.Style): StyledTextOutput = style(style).println(any.toString())

/**
 * Function extension to log info level output in specified style.
 */
fun StyledTextOutput.info(any: Any) = color(any, StyledTextOutput.Style.Description)

/**
 * Function extension to log success level output in specified style.
 */
fun StyledTextOutput.success(any: Any) = color(any, StyledTextOutput.Style.Success)

/**
 * Function extension to log error level output in specified style.
 */
fun StyledTextOutput.error(any: Any) = color(any, StyledTextOutput.Style.FailureHeader)
