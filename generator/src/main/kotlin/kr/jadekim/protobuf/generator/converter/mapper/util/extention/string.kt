package kr.jadekim.protobuf.generator.converter.mapper.util.extention

fun String.nameSuffix(suffix: String): String {
    if (endsWith("%N")) {
        return removeSuffix("%N") + "%L" + suffix
    }

    return this + suffix
}