package kr.jadekim.protobuf.converter.mapper

import com.google.protobuf.Descriptors
import com.google.protobuf.Message
import com.google.protobuf.Parser
import kr.jadekim.protobuf.converter.ProtobufConverter

interface ProtobufTypeMapper<OutputType, DelegatorType : Message> : ProtobufConverter<OutputType> {

    val descriptor: Descriptors.Descriptor

    val parser: Parser<DelegatorType>

    fun convert(obj: OutputType): DelegatorType

    fun convert(obj: DelegatorType): OutputType

    fun OutputType.toDelegator(): DelegatorType = convert(this)

    fun DelegatorType.fromDelegator(): OutputType = convert(this)

    override fun serialize(obj: OutputType): ByteArray = obj.toDelegator().toByteArray()

    override fun deserialize(bytes: ByteArray): OutputType = parser.parseFrom(bytes).fromDelegator()
}

fun <M> ByteArray.parseProtobuf(mapper: ProtobufTypeMapper<M, *>): M = mapper.deserialize(this)
