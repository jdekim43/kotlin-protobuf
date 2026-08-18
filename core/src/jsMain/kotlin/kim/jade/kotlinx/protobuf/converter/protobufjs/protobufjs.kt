@file:JsModule("protobufjs")

package kim.jade.kotlinx.protobuf.converter.protobufjs

import org.khronos.webgl.Uint8Array

external class Root {

    fun lookupType(path: String): Type

    fun lookup(path: String): dynamic

    companion object {

        fun fromDescriptor(descriptor: Uint8Array): Root
    }
}

external class Type {

    val fullName: String

    val fieldsArray: Array<Field>

    fun resolveAll(): Type

    fun encode(message: Any?): Writer

    fun decode(reader: Uint8Array): dynamic

    fun verify(message: Any?): String?

    fun toObject(message: Any?, options: dynamic): dynamic

    fun fromObject(`object`: dynamic): dynamic
}

external class Writer {

    fun finish(): Uint8Array
}

external interface Field {

    val name: String

    val repeated: Boolean

    val map: Boolean

    val resolvedType: dynamic
}

external val wrappers: dynamic

external val util: dynamic
