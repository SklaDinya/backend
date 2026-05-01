import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("java-library")
}

description = "services-email"
group = "skladinya.services.email"

dependencies {
    implementation(project(":domain"))

    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
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
        attributes["Bundle-Name"] = "SklaDinya Email Service"
        attributes["Bundle-SymbolicName"] = archiveBaseName
    }
    archiveBaseName.set("skladinya-services-email")
    archiveClassifier.set("")
    archiveVersion.set("")
}