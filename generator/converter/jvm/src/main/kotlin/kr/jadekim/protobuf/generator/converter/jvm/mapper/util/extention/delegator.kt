package kr.jadekim.protobuf.generator.converter.jvm.mapper.util.extention

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kr.jadekim.protobuf.generator.util.ProtobufWordSplitter
import kr.jadekim.protobuf.generator.util.extention.fileName
import kr.jadekim.protobuf.generator.util.extention.simpleNames
import net.pearx.kasechange.toPascalCase

val Descriptors.GenericDescriptor.delegatorPackage: String
    get() = if (file.options.javaPackage.isNullOrBlank()) file.`package` else file.options.javaPackage

val Descriptors.GenericDescriptor.delegatorOuterClassName: String
    get() {
        if (file.options.hasJavaOuterClassname()) {
            return file.options.javaOuterClassname
        }

        val candidate = fileName.toPascalCase(ProtobufWordSplitter)

        if (file.findMessageTypeByName(candidate) != null
            || file.findEnumTypeByName(candidate) != null
            || file.findServiceByName(candidate) != null
            || file.findExtensionByName(candidate) != null
        ) {
            return candidate + "OuterClass"
        }

        return candidate
    }

val Descriptors.GenericDescriptor.delegatorTypeName: ClassName
    get() {
        val simpleNames = simpleNames.toMutableList()

        if (!file.options.javaMultipleFiles) {
            simpleNames.add(0, delegatorOuterClassName)
        }

        return ClassName(delegatorPackage, simpleNames)
    }

val Descriptors.ServiceDescriptor.delegatorTypeName: ClassName
    get() = ClassName(delegatorPackage, name + "Grpc")

val String.delegatorNameEscaped: String
    get() = if (equals("class", true)) {
        this + "_"
    } else {
        this
    }
