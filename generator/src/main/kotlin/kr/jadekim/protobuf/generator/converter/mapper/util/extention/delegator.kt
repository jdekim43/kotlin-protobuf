package kr.jadekim.protobuf.generator.converter.mapper.util.extention

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kr.jadekim.protobuf.generator.util.extention.fileName
import kr.jadekim.protobuf.generator.util.extention.simpleNames
import net.pearx.kasechange.toPascalCase

val Descriptors.GenericDescriptor.delegatorTypeName: ClassName
    get() {
        val simpleNames = simpleNames.toMutableList()

        if (!file.options.javaMultipleFiles) {
            val outerClassName = if (file.options.hasJavaOuterClassname()) {
                file.options.javaOuterClassname
            } else {
                fileName.toPascalCase()
            }

            simpleNames.add(0, outerClassName)
        }

        return ClassName(file.options.javaPackage, simpleNames)
    }