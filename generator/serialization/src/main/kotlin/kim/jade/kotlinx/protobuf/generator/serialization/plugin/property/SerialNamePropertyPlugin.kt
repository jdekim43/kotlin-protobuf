package kim.jade.kotlinx.protobuf.generator.serialization.plugin.property

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import kim.jade.kotlinx.protobuf.generator.ImportName
import kim.jade.kotlinx.protobuf.generator.type.TypeGenerator
import kotlinx.serialization.SerialName

object SerialNamePropertyPlugin : TypeGenerator.PropertyPlugin {

    override fun applyToField(
        parameter: ParameterSpec.Builder,
        property: PropertySpec.Builder,
        imports: MutableSet<ImportName>,
        descriptor: Descriptors.FieldDescriptor,
    ) {
        parameter.addAnnotation(
            AnnotationSpec.builder(SerialName::class)
                .addMember("value = %S", descriptor.jsonName)
                .build()
        )
    }
}
