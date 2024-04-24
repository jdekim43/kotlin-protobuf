package kr.jadekim.protobuf.generator.type

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import kr.jadekim.protobuf.generator.ImportName
import kr.jadekim.protobuf.generator.util.ProtobufWordSplitter
import kr.jadekim.protobuf.generator.util.extention.outputTypeName
import net.pearx.kasechange.toCamelCase

class ServiceTypeGenerator : TypeGenerator<Descriptors.ServiceDescriptor> {

    override fun generate(descriptor: Descriptors.ServiceDescriptor): Pair<TypeSpec, Set<ImportName>> {
        val name = descriptor.outputTypeName
        val spec = TypeSpec.interfaceBuilder(name)
        val imports = mutableSetOf<ImportName>()

        for (method in descriptor.methods) {
            val functionName = method.name.toCamelCase(ProtobufWordSplitter)

            spec.addFunction(
                FunSpec.builder(functionName)
                    .addModifiers(KModifier.SUSPEND, KModifier.ABSTRACT)
                    .addParameter("request", method.inputType.outputTypeName)
                    .returns(method.outputType.outputTypeName)
                    .build()
            )
        }

        return spec.build() to imports
    }
}