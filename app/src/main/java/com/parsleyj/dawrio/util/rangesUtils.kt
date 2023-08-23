package com.parsleyj.dawrio.util

fun Float.toAngle(
    vRange: ClosedFloatingPointRange<Float>,
    aRange: ClosedFloatingPointRange<Float> = 0f..360f
): Float {
    val vRangeSize = vRange.endInclusive - vRange.start
    val aRangeSize = aRange.endInclusive - aRange.start
    return ((this - vRange.start) / vRangeSize) * aRangeSize + aRange.start
}

val ClosedFloatingPointRange<Float>.size:Float get() = this.endInclusive - this.start

fun Int.toFloatRange(): ClosedFloatingPointRange<Float> = 0f..this.toFloat()

//Modulation signal is in 0..1
//Value to be given to element has to be as scale