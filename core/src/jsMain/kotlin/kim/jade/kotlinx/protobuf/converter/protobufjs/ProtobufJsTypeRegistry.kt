package kim.jade.kotlinx.protobuf.converter.protobufjs

object ProtobufJsTypeRegistry {

    private val files = mutableListOf<ProtobufJsFile>()

    fun add(vararg files: ProtobufJsFile) {
        add(files.asIterable())
    }

    fun add(files: Iterable<ProtobufJsFile>) {
        for (file in files) {
            if (this.files.none { it.name == file.name }) {
                this.files += file
            }
        }
    }

    internal fun lookup(fullName: String): Type? {
        for (file in files) {
            file.lookupOrNull(fullName)?.let { return it }
        }

        return null
    }
}
