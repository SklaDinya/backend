plugins {
    id("java-library")
}

repositories {
    mavenCentral()
}

description = "tests-integration"
group = "skladinya.tests.integration"

dependencies {
    implementation(project(":domain"))
    implementation(project(":persistence-postgres"))
    implementation(project(":persistence-redis"))
    implementation(project(":services-email"))
    implementation(project(":tests-helper"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-data-redis")
    testImplementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    testImplementation("com.h2database:h2")
    testImplementation("com.icegreen:greenmail-junit5:2.1.8")
}

val copyEmailTemplates by tasks.registering(Copy::class) {
    from(project(":services-email").sourceSets["main"].resources + "/templates")
    include("*.html")
    into("src/test/resources/templates")
    dependsOn(tasks.processTestResources)
}

tasks.test {
    useJUnitPlatform()
    dependsOn(copyEmailTemplates)
}
