import sun.jvmstat.monitor.MonitoredVmUtil.mainClass

plugins {
    java
    application
}

group = "ra.coursemanagement"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.mindrot:jbcrypt:0.4")
}

application {
    mainClass.set("ra.coursemanagement.Main")
}

// Cú pháp thay thế an toàn không lo lỗi thiếu thư viện import
tasks.named<JavaCompile>("compileJava") {
    options.encoding = "UTF-8"
}