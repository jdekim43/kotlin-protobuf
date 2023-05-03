package kr.jadekim.protobuf.annotation

@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufSyntax(val syntax: String)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufIndex(val index: Int)
