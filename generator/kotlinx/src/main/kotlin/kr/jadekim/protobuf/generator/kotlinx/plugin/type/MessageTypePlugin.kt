package kr.jadekim.protobuf.generator.kotlinx.plugin.type

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.Import
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.jadekim.protobuf.generator.type.TypeGenerator
import kr.jadekim.protobuf.generator.util.extention.typeUrl

object MessageTypePlugin : TypeGenerator.Plugin<Descriptors.Descriptor> {

    override fun applyTo(spec: TypeSpec.Builder, imports: MutableSet<Import>, descriptor: Descriptors.Descriptor) {
        spec.addAnnotation(Serializable::class)
        spec.addAnnotation(
            AnnotationSpec.builder(SerialName::class)
                .addMember("value = %S", descriptor.typeUrl)
                .build()
        )
    }
}