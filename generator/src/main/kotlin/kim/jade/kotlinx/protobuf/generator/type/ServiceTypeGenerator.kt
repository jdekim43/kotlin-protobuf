package kim.jade.kotlinx.protobuf.generator.type

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import kim.jade.kotlinx.protobuf.generator.ImportName
import kim.jade.kotlinx.protobuf.generator.util.extention.addDeprecatedAnnotation
import kim.jade.kotlinx.protobuf.generator.util.extention.addDescriptorBytes
import kim.jade.kotlinx.protobuf.generator.util.extention.addMethodAnnotation
import kim.jade.kotlinx.protobuf.generator.util.extention.addOptionAnnotations
import kim.jade.kotlinx.protobuf.generator.util.extention.addServiceAnnotation
import kim.jade.kotlinx.protobuf.generator.util.extention.functionSpecBuilder
import kim.jade.kotlinx.protobuf.generator.util.extention.outputTypeName
import kim.jade.kotlinx.protobuf.type.ProtobufService

class ServiceTypeGenerator : TypeGenerator<Descriptors.ServiceDescriptor> {

    override fun generate(descriptor: Descriptors.ServiceDescriptor): Pair<TypeSpec, Set<ImportName>> {
        val name = descriptor.outputTypeName
        val spec = TypeSpec.interfaceBuilder(name).addSuperinterface(ProtobufService::class)
        val imports = mutableSetOf<ImportName>()

        spec.addServiceAnnotation(descriptor)
        spec.addOptionAnnotations(descriptor.options)

        if (descriptor.options.deprecated) {
            spec.addDeprecatedAnnotation("")
        }

        spec.addType(TypeSpec.companionObjectBuilder().addDescriptorBytes(descriptor).build())

        for (method in descriptor.methods) {
            spec.addFunction(
                method.functionSpecBuilder(KModifier.ABSTRACT)
                    .addMethodAnnotation(method)
                    .addOptionAnnotations(method.options)
                    .build()
            )
        }

        return spec.build() to imports
    }
}
