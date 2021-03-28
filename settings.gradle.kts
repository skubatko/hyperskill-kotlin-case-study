pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val springDependencyVersion: String by settings

        kotlin("multiplatform") version kotlinVersion apply false
        kotlin("jvm") version kotlinVersion apply false
        kotlin("js") version kotlinVersion apply false
        kotlin("kapt") version kotlinVersion apply false

        id("io.spring.dependency-management") version springDependencyVersion apply false
    }
}

rootProject.name = "hyperskill-kotlin-case-study"

include(
    "case6356"
)
