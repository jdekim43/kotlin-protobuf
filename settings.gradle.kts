
rootProject.name = "kotlin-protobuf"

include(
    ":kotlinx",
    ":generator",
    ":generator:converter",
    ":generator:converter:jvm",
    ":generator:converter:multiplatform",
    ":generator:converter:multiplatform:jvm",
    ":generator:kotlinx",
    ":generator:grpc",
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