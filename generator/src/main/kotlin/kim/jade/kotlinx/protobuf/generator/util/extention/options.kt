package kim.jade.kotlinx.protobuf.generator.util.extention

import com.google.protobuf.ByteString
import com.google.protobuf.Descriptors
import com.google.protobuf.Message
import com.google.protobuf.TextFormat
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import kim.jade.kotlinx.protobuf.annotation.ProtobufOption

data class ProtobufOptionEntry(val key: String, val value: String)

val Message.protobufOptionEntries: List<ProtobufOptionEntry>
    get() {
        val resolved = ProtobufOptionResolver.resolve(this)
        val entries = resolved.allFields.entries.flatMap { (field, value) ->
            val key = if (field.isExtension) "(${field.fullName})" else field.name

            if (field.isRepeated) {
                (value as List<*>).filterNotNull().map { ProtobufOptionEntry(key, it.renderAsOptionValue()) }
            } else {
                listOf(ProtobufOptionEntry(key, value.renderAsOptionValue()))
            }
        }

        val unknown = resolved.unknownFields

        if (unknown.asMap().isEmpty()) {
            return entries
        }

        return entries + ProtobufOptionEntry("(unknown)", unknown.toString().replace('\n', ' ').trim())
    }

private fun Any.renderAsOptionValue(): String = when (this) {
    is Descriptors.EnumValueDescriptor -> name
    is Message -> TextFormat.printer().emittingSingleLine(true).printToString(this).trim()
    is ByteString -> "\"${TextFormat.escapeBytes(this)}\""
    is String -> "\"${escapeOptionText()}\""
    else -> toString()
}

private fun String.escapeOptionText(): String = buildString {
    for (character in this@escapeOptionText) {
        when (character) {
            '\\' -> append("\\\\")
            '"' -> append("\\\"")
            '\n' -> append("\\n")
            '\r' -> append("\\r")
            '\t' -> append("\\t")
            else -> append(character)
        }
    }
}

private fun List<ProtobufOptionEntry>.optionAnnotations(): List<AnnotationSpec> = map {
    AnnotationSpec.builder(ProtobufOption::class)
        .addMember("key = %S, value = %S", it.key, it.value)
        .build()
}

fun FileSpec.Builder.addOptionAnnotations(options: Message): FileSpec.Builder =
    apply { options.protobufOptionEntries.optionAnnotations().forEach(::addAnnotation) }

fun TypeSpec.Builder.addOptionAnnotations(options: Message): TypeSpec.Builder =
    apply { options.protobufOptionEntries.optionAnnotations().forEach(::addAnnotation) }

fun FunSpec.Builder.addOptionAnnotations(options: Message): FunSpec.Builder =
    apply { options.protobufOptionEntries.optionAnnotations().forEach(::addAnnotation) }

fun ParameterSpec.Builder.addOptionAnnotations(
    options: Message,
    extraEntries: List<ProtobufOptionEntry> = emptyList(),
): ParameterSpec.Builder =
    apply { (options.protobufOptionEntries + extraEntries).optionAnnotations().forEach(::addAnnotation) }
