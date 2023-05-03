package kr.jadekim.protobuf.generator.file

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FileSpec

interface FileGenerator {

    fun generate(descriptor: Descriptors.FileDescriptor): FileSpec
}
