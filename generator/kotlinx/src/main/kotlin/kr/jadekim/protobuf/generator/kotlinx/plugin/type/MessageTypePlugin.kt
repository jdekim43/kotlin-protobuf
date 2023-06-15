package kr.jadekim.protobuf.generator.kotlinx.plugin.type

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kr.jadekim.protobuf.generator.ImportName
import kr.jadekim.protobuf.generator.converter.util.extention.converterTypeName
import kr.jadekim.protobuf.generator.type.TypeGenerator
import kr.jadekim.protobuf.generator.util.extention.outputTypeName
import kr.jadekim.protobuf.generator.util.extention.typeName
import kr.jadekim.protobuf.generator.util.extention.typeUrl
import kr.jadekim.protobuf.kotlinx.ProtobufConverterEncoder
import kr.jadekim.protobuf.kotlinx.ProtobufFormat
import kr.jadekim.protobuf.kotlinx.ProtobufMapperDecoder

object MessageTypePlugin : TypeGenerator.Plugin<Descriptors.Descriptor> {

    override fun applyTo(spec: TypeSpec.Builder, imports: MutableSet<ImportName>, descriptor: Descriptors.Descriptor) {
        spec.addAnnotation(
            AnnotationSpec.builder(Serializable::class)
                .addMember("with = %T::class", descriptor.outputTypeName.nestedClass("KotlinxSerializer"))
                .build()
        )
        spec.addAnnotation(
            AnnotationSpec.builder(SerialName::class)
                .addMember("value = %T.TYPE_URL", descriptor.outputTypeName)
                .build()
        )

        descriptor.writeSerializerTo(spec)
    }

    private fun Descriptors.Descriptor.writeSerializerTo(spec: TypeSpec.Builder) {
        val outputTypeName = outputTypeName
        val converterTypeName = converterTypeName

        TypeSpec.objectBuilder("KotlinxSerializer")
            .addSuperinterface(KSerializer::class.typeName.parameterizedBy(outputTypeName))
            .addProperty(
                PropertySpec.builder("delegator", KSerializer::class.typeName.parameterizedBy(outputTypeName))
                    .addModifiers(KModifier.PRIVATE)
                    .initializer("%T.serializer()", outputTypeName)
                    .build()
            )
            .addProperty(
                PropertySpec.builder("descriptor", SerialDescriptor::class)
                    .addModifiers(KModifier.OVERRIDE)
                    .initializer("delegator.descriptor")
                    .build()
            )
            .addFunction(
                FunSpec.builder("serialize")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("encoder", Encoder::class)
                    .addParameter("value", outputTypeName)
                    .beginControlFlow("if (encoder is %T)", ProtobufConverterEncoder::class)
                    .addStatement("encoder.encodeValue(%T.serialize(value))", converterTypeName)
                    .addStatement("return")
                    .endControlFlow()
                    .addStatement("delegator.serialize(encoder, value)")
                    .build()
            )
            .addFunction(
                FunSpec.builder("deserialize")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("decoder", Decoder::class)
                    .returns(outputTypeName)
                    .beginControlFlow("if (decoder is %T)", ProtobufMapperDecoder::class)
                    .addStatement("return %T.deserialize(decoder.decodeByteArray())", converterTypeName)
                    .endControlFlow()
                    .addStatement("return delegator.deserialize(decoder)")
                    .build()
            )
            .build()
            .let { spec.addType(it) }
    }
}