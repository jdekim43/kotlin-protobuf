
rootProject.name = "kotlin-protobuf"

include(
    ":kotlinx",
    ":grpc",
    ":generator",
    ":generator:converter",
    ":generator:converter:jvm",
    ":generator:converter:multiplatform",
    ":generator:converter:multiplatform:jvm",
    ":generator:kotlinx",
    ":generator:grpc",
    ":generator:grpc:jvm",
    ":generator:grpc:multiplatform",
    ":generator:grpc:multiplatform:jvm",
    ":example",
    ":example-multiplatform",
)

fun ProjectDescriptor.renameChildren() {
    children.forEach {
        val origin = it.name
        if (it.parent != null) {
            it.name = it.parent!!.name + "-" + it.name
        }
        println("$origin to ${it.name} : ${it.path}")
        it.renameChildren()
    }
}

rootProject.renameChildren()