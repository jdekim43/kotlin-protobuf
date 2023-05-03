package kr.jadekim.protobuf.util

val <T> T.asJavaType: T
    get() = this

val ULong?.asJavaType: Long?
    get() = this?.toLong()

val UInt?.asJavaType: Int?
    get() = this?.toInt()

val ULong.asJavaType: Long
    get() = toLong()

val UInt.asJavaType: Int
    get() = toInt()

val <T> T.asKotlinType: T
    get() = this

val Long.asKotlinType: ULong
    get() = toULong()

val Int.asKotlinType: UInt
    get() = toUInt()
