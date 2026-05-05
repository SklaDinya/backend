plugins {
    id("java")
    id("org.springframework.boot") version "3.5.14"
    id("io.spring.dependency-management") version "1.1.7"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    group = "skladinya"
    version = "1.0-SNAPSHOT"

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    repositories {
        mavenCentral()
        maven {
            url = uri("https://repo1.maven.org/maven2")
        }
        maven {
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
            mavenContent {
                snapshotsOnly()
            }
        }
    }
    tasks.register<Copy>("copyDependencies") {
        from(configurations.runtimeClasspath.get())
        into("${rootProject.projectDir}/build/libs")
    }

    tasks.register<Wrapper>("wrapper") {
        gradleVersion = "8.8"
    }

    tasks.register("prepareKotlinBuildScriptModel") {
    }
}