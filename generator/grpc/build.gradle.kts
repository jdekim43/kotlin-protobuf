allprojects {
    dependencies {
        implementation(project(fullPath(":grpc")))
    }
}

subprojects {
    dependencies {
        api(project(fullPath(":generator:grpc")))
    }
}
