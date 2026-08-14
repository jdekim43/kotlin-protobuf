package kim.jade.kotlinx.protobuf.generator.converter

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kim.jade.kotlinx.protobuf.converter.ProtobufConverter
import kim.jade.kotlinx.protobuf.generator.addTo
import kim.jade.kotlinx.protobuf.generator.converter.platform.PlatformConverterGenerator
import kim.jade.kotlinx.protobuf.generator.converter.platform.SinglePlatformGenerator
import kim.jade.kotlinx.protobuf.generator.converter.util.extention.converterTypeName
import kim.jade.kotlinx.protobuf.generator.converter.util.extention.outputConverterFileName
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.util.extention.addGeneratorVersionAnnotation
import kim.jade.kotlinx.protobuf.generator.util.extention.outputPackage
import kim.jade.kotlinx.protobuf.generator.util.extention.outputTypeName
import kim.jade.kotlinx.protobuf.generator.util.extention.typeName

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
                .receiver(outputTypeName.nestedClass("Companion"))
                .addParameter("any", ClassName("google.protobuf", "Any"))
                .addParameter(
                    ParameterSpec.builder(
                        "converter",
                        ProtobufConverter::class.typeName.parameterizedBy(outputTypeName)
                    )
                        .defaultValue("%T", converterTypeName)
                        .build()
                )
                .returns(outputTypeName)
                .addStatement(
                    "if (any.typeUrl != TYPE_URL) throw %T(%P)",
                    ClassName("kotlin", "IllegalStateException"),
                    "Expected \$TYPE_URL but the Any holds \${any.typeUrl}",
                )
                .addStatement(
                    "return any.value.%M(converter)",
                    MemberName("kim.jade.kotlinx.protobuf.converter", "parseProtobuf")
                )
                .build()
        )

        spec.addProperty(
            PropertySpec.builder("converter", converterTypeName)
                .receiver(outputTypeName.nestedClass("Companion"))
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement("return %T", converterTypeName)
                        .build()
                )
                .build()
        )
    }
}
