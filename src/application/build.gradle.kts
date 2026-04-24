import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("java-library")
}

description = "application"
group = "skladinya.application"

dependencies {
    implementation(project(":domain"))
    implementation(project(":persistence-postgres"))
    implementation(project(":persistence-redis"))
    implementation(project(":services-auth"))
    implementation(project(":services-jwt"))
    implementation(project(":services-user"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.projectlombok:lombok")
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
        attributes["Bundle-Name"] = "SklaDinya Application"
        attributes["Bundle-SymbolicName"] = archiveBaseName
    }
    archiveBaseName.set("skladinya-application")
    archiveClassifier.set("")
    archiveVersion.set("")
}