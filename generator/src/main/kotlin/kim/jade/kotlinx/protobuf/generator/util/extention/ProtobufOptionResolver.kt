package kim.jade.kotlinx.protobuf.generator.util.extention

import com.google.protobuf.Descriptors
import com.google.protobuf.DynamicMessage
import com.google.protobuf.ExtensionRegistry
import com.google.protobuf.Message

internal object ProtobufOptionResolver {

    private const val DESCRIPTOR_PROTO = "google/protobuf/descriptor.proto"

    private var registry: ExtensionRegistry = ExtensionRegistry.getEmptyRegistry()
    private var optionTypes: Map<String, Descriptors.Descriptor> = emptyMap()

    fun configure(files: Collection<Descriptors.FileDescriptor>, base: ExtensionRegistry) {
        registry = base
        optionTypes = files.firstOrNull { it.name == DESCRIPTOR_PROTO }
            ?.messageTypes
            ?.associateBy { it.fullName }
            .orEmpty()

        if (optionTypes.isEmpty()) {
            return
        }

        for (extension in files.flatMap { it.collectExtensions() }) {
            if (extension.containingType.fullName !in optionTypes) {
                continue
            }

            if (extension.javaType == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                registry.add(extension, DynamicMessage.getDefaultInstance(extension.messageType))
            } else {
                registry.add(extension)
            }
        }
    }

    fun resolve(options: Message): Message {
        val type = optionTypes[options.descriptorForType.fullName] ?: return options

        if (options.unknownFields.asMap().isEmpty()) {
            return options
        }

        return runCatching { DynamicMessage.parseFrom(type, options.toByteArray(), registry) }.getOrDefault(options)
    }

    private fun Descriptors.FileDescriptor.collectExtensions(): List<Descriptors.FieldDescriptor> =
        extensions + messageTypes.flatMap { it.collectExtensions() }

    private fun Descriptors.Descriptor.collectExtensions(): List<Descriptors.FieldDescriptor> =
        extensions + nestedTypes.flatMap { it.collectExtensions() }
}
