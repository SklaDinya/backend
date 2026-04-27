plugins {
    id("java-library")
}

description = "utils-bdfiller"
group = "skladinya.utils.filler"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.security:spring-security-crypto:6.4.4")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.apache.logging.log4j:log4j-api:2.23.1")
    implementation("net.datafaker:datafaker:1.5.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.opencsv:opencsv:5.12.0")
}

tasks.test {
    useJUnitPlatform()
}
