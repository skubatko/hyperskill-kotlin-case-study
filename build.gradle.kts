import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    kotlin("jvm")
    id("io.spring.dependency-management")
}

group = "ru.skubatko.dev.hyperskill"
description = "Hyperskill Kotlin course"
version = "0.0.1"

allprojects {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
}

subprojects {
    apply(plugin = "org.gradle.idea")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.spring.dependency-management")

    idea {
        module {
            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }

    dependencies {
        val slf4jVersion: String by project
        val logbackEncoderVersion: String by project

        implementation(kotlin("stdlib-jdk8"))
        implementation("org.slf4j:slf4j-api:$slf4jVersion")
        implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")

        testImplementation(kotlin("test"))
        testImplementation(kotlin("test-junit"))
    }

    val javaVersion: String by project

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = javaVersion
            }
        }
    }
}
