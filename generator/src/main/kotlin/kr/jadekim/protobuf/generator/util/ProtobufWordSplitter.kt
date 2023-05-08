package kr.jadekim.protobuf.generator.util

import net.pearx.kasechange.splitter.WordSplitter
import net.pearx.kasechange.splitter.WordSplitterConfig
import net.pearx.kasechange.splitter.WordSplitterConfigurable

object ProtobufWordSplitter : WordSplitter {

    private val delegator = WordSplitterConfigurable(
        WordSplitterConfig(
            boundaries = setOf(' ', '-', '_', '.'),
            handleCase = true,
            treatDigitsAsUppercase = false,
        )
    )

    override fun splitToWords(string: String): List<String> = delegator.splitToWords(string)
        .flatMap { it.splitDigits() }
        .flatMap { it.splitAbbreviation() }

    private fun String.splitDigits(): List<String> {
        val result = mutableListOf<String>()
        var addIndex = 0

        for ((i, c) in withIndex()) {
            if (c.isDigit()) {
                result.add(substring(addIndex..i))
                addIndex = i + 1
            }
        }

        if (addIndex != length) {
            result.add(substring(addIndex until length))
        }

        return result
    }

    // If use with special chars, should disable this function.
    // Because uppercase chars will be split
    private fun String.splitAbbreviation(): List<String> {
        if (all { it.isUpperCase() }) {
            return map { it.toString() }
        }

        return listOf(this)
    }
}