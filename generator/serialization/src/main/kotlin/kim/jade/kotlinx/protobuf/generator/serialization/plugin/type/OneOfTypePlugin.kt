package kim.jade.kotlinx.protobuf.generator.serialization.plugin.type

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.Serializable
import kim.jade.kotlinx.protobuf.generator.ImportName
import kim.jade.kotlinx.protobuf.generator.type.TypeGenerator

object OneOfTypePlugin : TypeGenerator.Plugin<Descriptors.OneofDescriptor> {

    override fun applyTo(
        spec: TypeSpec.Builder,
        imports: MutableSet<ImportName>,
        descriptor: Descriptors.OneofDescriptor,
    ) {
        spec.addAnnotation(Serializable::class)
    }
}

object OneOfItemTypePlugin : TypeGenerator.Plugin<Descriptors.FieldDescriptor> {

    override fun applyTo(
        spec: TypeSpec.Builder,
        imports: MutableSet<ImportName>,
        descriptor: Descriptors.FieldDescriptor
    ) {
// Convertable only base type.
//        spec.addAnnotation(Serializable::class)
    }
}
