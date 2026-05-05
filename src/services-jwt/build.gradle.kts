import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("java-library")
}

description = "services-jwt"
group = "skladinya.services.jwt"

dependencies {
    implementation(project(":domain"))
    implementation("com.auth0:java-jwt:4.5.1")
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
        attributes["Bundle-Name"] = "SklaDinya JWT Service"
        attributes["Bundle-SymbolicName"] = archiveBaseName
    }
    archiveBaseName.set("skladinya-services-jwt")
    archiveClassifier.set("")
    archiveVersion.set("")
}