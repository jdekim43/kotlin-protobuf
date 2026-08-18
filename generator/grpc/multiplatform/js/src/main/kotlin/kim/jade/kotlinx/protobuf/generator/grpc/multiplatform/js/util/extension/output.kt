package kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.js.util.extension

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName
import kim.jade.kotlinx.protobuf.generator.grpc.util.extension.grpcTypeName
import kim.jade.kotlinx.protobuf.generator.grpc.util.extension.outputGrpcPackageName
import kim.jade.kotlinx.protobuf.generator.util.extention.fileName

val Descriptors.FileDescriptor.outputJsGrpcPackageName: String
    get() = "$outputGrpcPackageName.js"

val Descriptors.FileDescriptor.outputJsGrpcFileName: String
    get() = "$fileName.kt"

val Descriptors.ServiceDescriptor.jsGrpcTypeName: ClassName
    get() = ClassName(file.outputJsGrpcPackageName, grpcTypeName.simpleNames.map { it + "Js" })

object JsGrpcRuntime {

    private const val GRPC_PACKAGE = "kim.jade.kotlinx.protobuf.grpc"

    val GRPC_METHOD: ClassName = ClassName(GRPC_PACKAGE, "GrpcMethod")

    val GRPC_SERVICE_BINDING: ClassName = ClassName(GRPC_PACKAGE, "GrpcServiceBinding")

    val GRPC_STATUS: ClassName = ClassName(GRPC_PACKAGE, "GrpcStatus")

    val GRPC_STATUS_EXCEPTION: ClassName = ClassName(GRPC_PACKAGE, "GrpcStatusException")

    val GRPC_SERVICE: MemberName = MemberName(GRPC_PACKAGE, "grpcService")

    val UNARY_CALL: MemberName = MemberName(GRPC_PACKAGE, "unaryCall", true)

    val SERVER_STREAMING_CALL: MemberName = MemberName(GRPC_PACKAGE, "serverStreamingCall", true)

    val CLIENT_STREAMING_CALL: MemberName = MemberName(GRPC_PACKAGE, "clientStreamingCall", true)

    val BIDI_STREAMING_CALL: MemberName = MemberName(GRPC_PACKAGE, "bidiStreamingCall", true)
}

val Descriptors.MethodDescriptor.serviceBuilderFunctionName: String
    get() = when {
        isClientStreaming && isServerStreaming -> "bidiStreaming"
        isClientStreaming -> "clientStreaming"
        isServerStreaming -> "serverStreaming"
        else -> "unary"
    }

val Descriptors.MethodDescriptor.clientCallMemberName: MemberName
    get() = when {
        isClientStreaming && isServerStreaming -> JsGrpcRuntime.BIDI_STREAMING_CALL
        isClientStreaming -> JsGrpcRuntime.CLIENT_STREAMING_CALL
        isServerStreaming -> JsGrpcRuntime.SERVER_STREAMING_CALL
        else -> JsGrpcRuntime.UNARY_CALL
    }
