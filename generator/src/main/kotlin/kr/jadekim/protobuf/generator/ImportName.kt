package kr.jadekim.protobuf.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.MemberName

data class ImportName(
    val packageName: String,
    val simpleName: String,
    val alias: String? = null,
) {

    constructor(
        typeName: ClassName,
        alias: String? = null,
    ) : this(typeName.packageName, typeName.simpleNames.first(), alias)

    constructor(
        memberName: MemberName,
        alias: String? = null,
    ) : this(memberName.packageName, memberName.simpleName, alias)

    fun addTo(spec: FileSpec.Builder) {
        if (alias.isNullOrBlank()) {
            spec.addImport(packageName, simpleName)
        } else {
            spec.addAliasedImport(MemberName(packageName, simpleName), alias)
        }
    }
}

fun Collection<ImportName>.addTo(spec: FileSpec.Builder) {
    forEach { it.addTo(spec) }
}
