package kim.jade.kotlinx.protobuf.converter.protobufjs

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.set
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class ProtobufJsFile(
    val name: String,
    private val descriptor: String,
    private val dependencies: List<ProtobufJsFile> = emptyList(),
) {

    private val types = mutableMapOf<String, Type>()

    private val root: Root by lazy {
        checkNotNull(descriptorExtension) {
            "protobufjs/ext/descriptor did not load, so Root.fromDescriptor is missing. It ships inside " +
                "the protobufjs npm package; a bundler configured to drop it will break every converter."
        }

        Root.fromDescriptor(descriptorSet())
    }

    fun lookupType(fullName: String): Type = types.getOrPut(fullName) { root.lookupType(fullName) }

    internal fun lookupOrNull(fullName: String): Type? {
        types[fullName]?.let { return it }

        val found: dynamic = root.lookup(fullName)

        if (found == null || jsTypeOf(found.fieldsArray) == "undefined") {
            return null
        }

        val type = found.unsafeCast<Type>()
        types[fullName] = type

        return type
    }

    private fun collectInto(files: LinkedHashMap<String, String>) {
        if (files.containsKey(name)) {
            return
        }

        for (dependency in dependencies) {
            dependency.collectInto(files)
        }

        files[name] = descriptor
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun descriptorSet(): Uint8Array {
        val files = LinkedHashMap<String, String>()
        collectInto(files)

        return encodeDescriptorSet(files.values.map(Base64::decode))
    }

    private companion object {

        init {
            WellKnownJson.register()
        }

        private const val FILE_FIELD_TAG: Byte = 0x0A

        fun encodeDescriptorSet(files: List<ByteArray>): Uint8Array {
            var size = 0

            for (file in files) {
                size += 1 + varIntSize(file.size) + file.size
            }

            val bytes = Uint8Array(size)
            var offset = 0

            for (file in files) {
                bytes[offset++] = FILE_FIELD_TAG
                offset = writeVarInt(bytes, offset, file.size)

                for (byte in file) {
                    bytes[offset++] = byte
                }
            }

            return bytes
        }

        fun varIntSize(value: Int): Int {
            var size = 1
            var rest = value ushr 7

            while (rest != 0) {
                size++
                rest = rest ushr 7
            }

            return size
        }

        fun writeVarInt(bytes: Uint8Array, offset: Int, value: Int): Int {
            var position = offset
            var rest = value

            while (rest and 0x7F.inv() != 0) {
                bytes[position++] = ((rest and 0x7F) or 0x80).toByte()
                rest = rest ushr 7
            }

            bytes[position++] = rest.toByte()

            return position
        }
    }
}
