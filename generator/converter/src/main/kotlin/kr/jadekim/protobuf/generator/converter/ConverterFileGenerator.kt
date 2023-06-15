package kr.jadekim.protobuf.generator.converter

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.generator.addTo
import kr.jadekim.protobuf.generator.converter.platform.PlatformConverterGenerator
import kr.jadekim.protobuf.generator.converter.platform.SinglePlatformGenerator
import kr.jadekim.protobuf.generator.converter.util.extention.converterTypeName
import kr.jadekim.protobuf.generator.converter.util.extention.outputConverterFileName
import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.util.extention.addGeneratorVersionAnnotation
import kr.jadekim.protobuf.generator.util.extention.outputPackage
import kr.jadekim.protobuf.generator.util.extention.outputTypeName
import kr.jadekim.protobuf.generator.util.extention.typeName

class ConverterFileGenerator(
    val platformGenerator: PlatformConverterGenerator<Descriptors.Descriptor>,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputPackage, descriptor.outputConverterFileName)
        spec.addFileComment("Transform from %L", descriptor.name)

        spec.addGeneratorVersionAnnotation()

        for (messageDescriptor in descriptor.messageTypes) {
            val (specs, imports) = platformGenerator.generate(messageDescriptor)
            imports.addTo(spec)
            specs.forEach(spec::addType)

            if (!(platformGenerator is SinglePlatformGenerator && platformGenerator.isActual)) {
                messageDescriptor.writeUtilFunctionsTo(spec)
            }
        }

        return spec.build()
    }

    private fun Descriptors.Descriptor.writeUtilFunctionsTo(spec: FileSpec.Builder) {
        spec.addFunction(
            FunSpec.builder("toAny")
                .receiver(outputTypeName)
                .returns(ClassName("google.protobuf", "Any"))
                .addStatement(
                    "return %T(%T.TYPE_URL, with(%T) { toByteArray() })",
                    ClassName("google.protobuf", "Any"),
                    outputTypeName,
                    converterTypeName,
                )
                .build()
        )

        spec.addFunction(
            FunSpec.builder("parse")
                .receiver(ClassName("google.protobuf", "Any"))
                .returns(outputTypeName)
                .addParameter("converter", ProtobufConverter::class.typeName.parameterizedBy(outputTypeName))
                .addStatement(
                    "if (typeUrl != %T.TYPE_URL) throw %T(%S)",
                    outputTypeName,
                    IllegalStateException::class,
                    "Please check the type_url",
                )
                .addStatement("return value.%M(converter)", MemberName("kr.jadekim.protobuf.converter", "parseProtobuf"))
                .build()
        )
    }
}
