
rootProject.name = "kotlin-protobuf"

include(
    ":kotlinx",
    ":generator:grpc",
    ":generator:kotlinx",
    ":example",
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