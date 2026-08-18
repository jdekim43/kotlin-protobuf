package kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.mapper.util.extention.*
import kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.util.extention.outputJsDelegatorFileName
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.util.extention.addGeneratorVersionAnnotation

object JsDelegatorFileGenerator : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.delegatorPackage, descriptor.outputJsDelegatorFileName)
        spec.addFileComment("Transform from %L", descriptor.name)

        spec.addGeneratorVersionAnnotation()

        for (messageDescriptor in descriptor.messageTypes) {
            spec.addType(messageDescriptor.generate(isNested = false))
        }

        return spec.build()
    }

    private fun Descriptors.Descriptor.generate(isNested: Boolean): TypeSpec {
        val spec = TypeSpec.interfaceBuilder(delegatorTypeName.simpleNames.last())
        spec.addSuperinterface(JsRuntime.PROTOBUF_JS_MESSAGE)

        if (!isNested) {
            spec.addModifiers(KModifier.EXTERNAL)
        }

        for (field in fields.filterNot { it.isExtension }) {
            spec.addProperty(
                PropertySpec.builder(field.delegatorPropertyName, field.delegatorFieldTypeName)
                    .mutable()
                    .build()
            )
        }

        for (oneOf in realOneofs) {
            spec.addProperty(
                PropertySpec.builder(oneOf.delegatorPropertyName, STRING.copy(nullable = true))
                    .mutable()
                    .build()
            )
        }

        for (nestedType in nestedTypes.filterNot { it.options.mapEntry }) {
            spec.addType(nestedType.generate(isNested = true))
        }

        return spec.build()
    }
}
