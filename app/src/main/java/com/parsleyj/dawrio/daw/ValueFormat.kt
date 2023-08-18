package com.parsleyj.dawrio.daw

sealed class ValueFormat(val convertToString: (v: Float) -> String) {
    class Percent(min: Float, max: Float) : ValueFormat({
        val range = min..max
        val rangeExtent = range.endInclusive - range.start
        String.format("%.1f%", (it - range.start) / rangeExtent)
    })

    object Frequency : ValueFormat({ String.format("%.1f Hz", it) })

    class Decimal(val decimals: Int) : ValueFormat({ String.format("%.${decimals}f", it) })

    class Options(values: List<String>) : ValueFormat({ values[it.toInt() % values.size] })

    companion object {
        inline fun <reified E : Enum<E>> Options(): Options =
            Options(enumValues<E>().map { it.name })
    }
}