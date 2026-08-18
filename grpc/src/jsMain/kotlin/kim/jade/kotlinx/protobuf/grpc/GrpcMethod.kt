package kim.jade.kotlinx.protobuf.grpc

import kim.jade.kotlinx.protobuf.converter.ProtobufConverter
import kim.jade.kotlinx.protobuf.type.ProtobufMessage
import kim.jade.kotlinx.protobuf.util.toByteArray
import kim.jade.kotlinx.protobuf.util.toUint8Array
import org.khronos.webgl.Uint8Array

class GrpcMethod<Request : ProtobufMessage, Response : ProtobufMessage>(
    val path: String,
    val requestConverter: ProtobufConverter<Request>,
    val responseConverter: ProtobufConverter<Response>,
    val requestStream: Boolean,
    val responseStream: Boolean,
) {

    internal val serializeRequest: (Any?) -> Uint8Array =
        { requestConverter.serialize(it.unsafeCast<Request>()).toBuffer() }

    internal val deserializeRequest: (Uint8Array) -> Any? =
        { requestConverter.deserialize(it.toByteArray()) }

    internal val serializeResponse: (Any?) -> Uint8Array =
        { responseConverter.serialize(it.unsafeCast<Response>()).toBuffer() }

    internal val deserializeResponse: (Uint8Array) -> Any? =
        { responseConverter.deserialize(it.toByteArray()) }
}

@JsName("Buffer")
private external object NodeBuffer {

    fun from(array: Uint8Array): Uint8Array
}

private fun ByteArray.toBuffer(): Uint8Array = NodeBuffer.from(toUint8Array())
