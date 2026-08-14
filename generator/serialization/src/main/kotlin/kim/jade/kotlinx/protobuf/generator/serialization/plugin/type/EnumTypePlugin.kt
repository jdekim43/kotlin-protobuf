package kim.jade.kotlinx.protobuf.generator.serialization.plugin.type

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kim.jade.kotlinx.protobuf.generator.ImportName
import kim.jade.kotlinx.protobuf.generator.type.TypeGenerator
import kim.jade.kotlinx.protobuf.generator.util.extention.outputTypeName

object EnumTypePlugin : TypeGenerator.Plugin<Descriptors.EnumDescriptor> {

    override fun applyTo(
        spec: TypeSpec.Builder,
        imports: MutableSet<ImportName>,
        descriptor: Descriptors.EnumDescriptor,
    ) {
        spec.addAnnotation(Serializable::class)
        spec.addAnnotation(
            AnnotationSpec.builder(SerialName::class)
                .addMember("value = %T.TYPE_URL", descriptor.outputTypeName)
                .build()
        )
    }
}