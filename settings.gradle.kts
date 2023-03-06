rootProject.name = "exchange-rate"

pluginManagement {
    val detektVersion: String by settings
    val dokkaVersion: String by settings
    val kotlinVersion: String by settings
    val testLoggerVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.adarshr.test-logger" -> useVersion(testLoggerVersion)
                "io.gitlab.arturbosch.detekt" -> useVersion(detektVersion)
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
                "org.jetbrains.dokka" -> useVersion(dokkaVersion)
            }
        }
    }
}
