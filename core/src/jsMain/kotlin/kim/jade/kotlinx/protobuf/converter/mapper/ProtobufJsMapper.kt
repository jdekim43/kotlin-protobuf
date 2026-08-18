package kim.jade.kotlinx.protobuf.converter.mapper

import kim.jade.kotlinx.protobuf.converter.ProtobufConverter
import kim.jade.kotlinx.protobuf.converter.protobufjs.ProtobufJsFile
import kim.jade.kotlinx.protobuf.converter.protobufjs.ProtobufJsMessage
import kim.jade.kotlinx.protobuf.type.ProtobufMessage
import kim.jade.kotlinx.protobuf.util.toByteArray
import kim.jade.kotlinx.protobuf.util.toUint8Array

interface ProtobufJsMapper<OutputType : ProtobufMessage, DelegatorType : ProtobufJsMessage> :
    ProtobufConverter<OutputType> {

    val typeName: String

    val protobufJsFile: ProtobufJsFile

    fun convert(obj: OutputType): DelegatorType

    fun convert(obj: DelegatorType): OutputType

    fun OutputType.toDelegator(): DelegatorType = convert(this)

    fun DelegatorType.fromDelegator(): OutputType = convert(this)

    override fun serialize(obj: OutputType): ByteArray =
        protobufJsFile.lookupType(typeName).encode(obj.toDelegator()).finish().toByteArray()

    override fun deserialize(bytes: ByteArray): OutputType =
        protobufJsFile.lookupType(typeName).decode(bytes.toUint8Array()).unsafeCast<DelegatorType>().fromDelegator()
}
