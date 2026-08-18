package kim.jade.kotlinx.protobuf.generator.util.extention

import com.google.protobuf.Descriptors

val Descriptors.GenericDescriptor.simpleName: String
    get() = fullName.removePrefix(file.`package` + '.')

val Descriptors.GenericDescriptor.simpleNames: List<String>
    get() = simpleName.split('.')

val Descriptors.GenericDescriptor.names: List<String>
    get() = listOf(file.`package`) + simpleNames

internal fun getTypeUrlPrefix(): String {
    var prefix = System.getProperty("kotlinx-protobuf.prefix")

    if (prefix.isNullOrBlank()) {
        prefix = System.getenv("KOTLIN_PROTOBUF_PREFIX")

        if (prefix.isNullOrBlank()) {
            return ""
        }
    }

    return prefix
}

val Descriptors.GenericDescriptor.typeUrl: String
    get() = "${getTypeUrlPrefix()}/$fullName"

val Descriptors.GenericDescriptor.fileName: String
    get() = file.name.split("/").last().removeSuffix(".proto")

val Descriptors.Descriptor.realFields: List<Descriptors.FieldDescriptor>
    get() {
        val oneOfFieldNames = realOneofs.flatMap { it.fields }.map { it.name }

        return fields.filterNot { oneOfFieldNames.contains(it.name) || it.isExtension }
    }

val Descriptors.FieldDescriptor.isOneOfField: Boolean
    get() = containingOneof != null

val Descriptors.FieldDescriptor.protoAccessorName: String
    get() = if (type == Descriptors.FieldDescriptor.Type.GROUP) messageType.name else name

val Descriptors.FieldDescriptor.isNullable: Boolean
    get() {
        if (isRepeated || isRequired || !hasPresence()) {
            return false
        }

        return containingOneof == null || toProto().proto3Optional
    }

private const val MAP_KEY_FIELD_NUMBER = 1
private const val MAP_VALUE_FIELD_NUMBER = 2

val Descriptors.FieldDescriptor.mapKeyField: Descriptors.FieldDescriptor
    get() = messageType.findFieldByNumber(MAP_KEY_FIELD_NUMBER)

val Descriptors.FieldDescriptor.mapValueField: Descriptors.FieldDescriptor
    get() = messageType.findFieldByNumber(MAP_VALUE_FIELD_NUMBER)
