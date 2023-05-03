package kr.jadekim.protobuf.type

data class Any<T>(
    val typeUrl: String,
    val bytes: ByteArray,
) {

    override fun equals(other: kotlin.Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Any<*>

        if (typeUrl != other.typeUrl) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        var result = typeUrl.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
