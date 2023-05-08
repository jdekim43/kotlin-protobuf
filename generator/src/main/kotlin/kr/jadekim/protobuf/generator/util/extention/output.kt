package kr.jadekim.protobuf.generator.util.extention

import com.google.protobuf.Descriptors
import kr.jadekim.protobuf.generator.util.ProtobufWordSplitter
import net.pearx.kasechange.toCamelCase
import net.pearx.kasechange.toPascalCase

val Descriptors.GenericDescriptor.outputPackage: String
    get() = file.`package`

val Descriptors.FileDescriptor.outputFileName: String
    get() = "$fileName.kt"

val Descriptors.FileDescriptor.outputGrpcFileName: String
    get() = "$fileName.grpc.kt"

val Descriptors.GenericDescriptor.outputSimpleName: String
    get() = simpleName

val Descriptors.GenericDescriptor.outputSimpleNames: List<String>
    get() = simpleNames

val Descriptors.GenericDescriptor.outputNames: List<String>
    get() = names

val Descriptors.FieldDescriptor.outputVariableNameString: String
    get() = name.toCamelCase(ProtobufWordSplitter)

val Descriptors.OneofDescriptor.outputVariableNameString: String
    get() = name.toCamelCase(ProtobufWordSplitter)

val Descriptors.FieldDescriptor.outputOneOfItemTypeNameString: String
    get() = name.toPascalCase(ProtobufWordSplitter)
