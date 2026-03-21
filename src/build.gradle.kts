plugins {
    id("java")
}

subprojects {
    apply(plugin = "java")

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