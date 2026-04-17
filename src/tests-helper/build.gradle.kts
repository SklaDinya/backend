import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("java-library")
}

description = "tests-helper"
group = "skladinya.tests.helper"

dependencies {
    implementation(project(":domain"))
}

tasks.jar {
    manifest {
        var date = Date()
        var javaVersion = System.getProperty("java.version")
        var vmVendor = System.getProperty("java.vm.vendor")
        var vmVersion = System.getProperty("java.vm.version")
        attributes["Created-By"] = String.format("%s (%s %s)", javaVersion, vmVendor, vmVersion)
        attributes["Gradle-Version"] = "Gradle $gradle.gradleVersion"
        attributes["Build-Date"] = SimpleDateFormat("yyyy-MM-dd").format(date)
        attributes["Build-Time"] = SimpleDateFormat("HH:mm:ss.SSSZ").format(date)
        attributes["Build-By"] = "FRANK"
        attributes["Bundle-Name"] = "SklaDinya Tests Helper"
        attributes["Bundle-SymbolicName"] = archiveBaseName
    }
    archiveBaseName.set("skladinya-tests-helper")
    archiveClassifier.set("")
    archiveVersion.set("")
}