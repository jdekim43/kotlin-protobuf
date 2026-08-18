package kim.jade.kotlinx.protobuf.generator.util.extention

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec

enum class EqualityStrategy {
    PLAIN,
    BYTES,
    BYTES_LIST,
    BYTES_MAP,
}

data class EqualityProperty(
    val name: String,
    val strategy: EqualityStrategy,
    val nullable: Boolean = false,
)

val Descriptors.FieldDescriptor.equalityStrategy: EqualityStrategy
    get() {
        if (isMapField) {
            return if (mapValueField.type == Descriptors.FieldDescriptor.Type.BYTES) {
                EqualityStrategy.BYTES_MAP
            } else {
                EqualityStrategy.PLAIN
            }
        }

        if (type != Descriptors.FieldDescriptor.Type.BYTES) {
            return EqualityStrategy.PLAIN
        }

        return if (isRepeated) EqualityStrategy.BYTES_LIST else EqualityStrategy.BYTES
    }

fun Descriptors.FieldDescriptor.equalityProperty(name: String): EqualityProperty =
    EqualityProperty(name, equalityStrategy, isNullable)

fun TypeSpec.Builder.addContentEquality(typeName: TypeName, properties: List<EqualityProperty>) {
    if (properties.none { it.strategy != EqualityStrategy.PLAIN }) {
        return
    }

    val equals = FunSpec.builder("equals")
        .addModifiers(KModifier.OVERRIDE)
        .addParameter("other", ANY.copy(nullable = true))
        .returns(BOOLEAN)
        .addStatement("if (this === other) return true")
        .addStatement("if (other !is %T) return false", typeName)

    properties.forEach { equals.addComparison(it) }

    addFunction(equals.addStatement("return true").build())

    val hashCode = FunSpec.builder("hashCode")
        .addModifiers(KModifier.OVERRIDE)
        .returns(INT)
        .addStatement("var result = %L", properties.first().hash())

    properties.drop(1).forEach { hashCode.addStatement("result = 31 * result + %L", it.hash()) }

    addFunction(hashCode.addStatement("return result").build())
}

private fun FunSpec.Builder.addComparison(property: EqualityProperty) {
    val name = property.name

    when (property.strategy) {
        EqualityStrategy.PLAIN ->
            addStatement("if (%N != other.%N) return false", name, name)

        EqualityStrategy.BYTES ->
            addStatement("if (!%N.contentEquals(other.%N)) return false", name, name)

        EqualityStrategy.BYTES_LIST -> {
            addStatement("if (%N.size != other.%N.size) return false", name, name)
            addStatement(
                "if (%N.indices.any { !%N[it].contentEquals(other.%N[it]) }) return false",
                name,
                name,
                name,
            )
        }

        EqualityStrategy.BYTES_MAP -> {
            addStatement("if (%N.size != other.%N.size) return false", name, name)
            addStatement(
                "if (%N.any { (entryKey, entryValue) -> !entryValue.contentEquals(other.%N[entryKey]) }) " +
                    "return false",
                name,
                name,
            )
        }
    }
}

private fun EqualityProperty.hash(): CodeBlock = when (strategy) {
    EqualityStrategy.PLAIN ->
        if (nullable) CodeBlock.of("(%N?.hashCode() ?: 0)", name) else CodeBlock.of("%N.hashCode()", name)

    EqualityStrategy.BYTES -> CodeBlock.of("%N.contentHashCode()", name)

    EqualityStrategy.BYTES_LIST ->
        CodeBlock.of("%N.fold(0) { hash, element -> 31 * hash + element.contentHashCode() }", name)

    EqualityStrategy.BYTES_MAP -> CodeBlock.of(
        "%N.entries.fold(0) { hash, entry -> hash + (entry.key.hashCode() xor entry.value.contentHashCode()) }",
        name,
    )
}
