package kr.jadekim.protobuf.generator.util.extention

import com.google.protobuf.Descriptors

val Descriptors.GenericDescriptor.simpleName: String
    get() = fullName.removePrefix(file.`package` + '.')

val Descriptors.GenericDescriptor.simpleNames: List<String>
    get() = simpleName.split('.')

val Descriptors.GenericDescriptor.names: List<String>
    get() = listOf(file.`package`) + simpleNames

internal fun getTypeUrlPrefix(): String {
    var prefix = System.getProperty("kotlin-protobuf.prefix")

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

val Descriptors.FieldDescriptor.isNullable: Boolean
    get() = hasPresence() && containingOneof != null && containingOneof.isSynthetic
