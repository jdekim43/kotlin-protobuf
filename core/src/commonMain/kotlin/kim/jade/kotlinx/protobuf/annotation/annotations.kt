package kim.jade.kotlinx.protobuf.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufMessage(val typeUrl: String)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufIndex(val index: Int)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufJsonName(val jsonName: String)

@Target(
    AnnotationTarget.FILE,
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class ProtobufOption(val key: String, val value: String)

@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class GeneratorVersion(val version: String)
