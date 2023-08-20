package com.parsleyj.dawrio.daw.element

import com.parsleyj.dawrio.daw.ValueFormat
import com.parsleyj.dawrio.daw.elementroute.ElementInPort
import com.parsleyj.dawrio.daw.elementroute.ElementOutPort

class ValueAdder(
    label: String,
    initialScale: Float = 1f,
    initialOffset: Float = 0f,
    clip: Boolean = false,
    clipStart: Float = 0f,
    clipEnd: Float = 1f,
    description: String = "",
    handle: ElementHandle = ElementHandle(
        createValueAdder(
            initialScale,
            initialOffset,
            clip,
            clipStart,
            clipEnd
        )
    )
) : Element(label, description, handle) {

    constructor(
        label: String,
        initialScale: Float = 1f,
        initialOffset: Float = 0f,
        clippingRange: ClosedFloatingPointRange<Float> =0f..1f,
        description: String = "",
    ) : this(
        label,
        initialScale,
        initialOffset,
        true,
        clippingRange.start,
        clippingRange.endInclusive,
        description
    )

    val outValue = ElementOutPort(this, "output", 0, ValueFormat.NumericWithDecimals(1))
    val input1 = ElementInPort(this, "input1", 0, ValueFormat.NumericWithDecimals(1))
    val input2 = ElementInPort(this, "input2", 1, ValueFormat.NumericWithDecimals(1))

    override val allInputs: List<ElementInPort>
        get() = listOf(input1, input2)

    override val allOutputs: List<ElementOutPort>
        get() = listOf(outValue)

    var scale: Float
        get() = getScale2(handle.toAddress)
        set(value) = setScale2(handle.toAddress, value)

    var offset: Float
        get() = getOffset2(handle.toAddress)
        set(value) = setOffset2(handle.toAddress, value)

    companion object {
        external fun createValueAdder(
            initialScale: Float,
            initialOffset: Float,
            clip: Boolean,
            clipStart: Float,
            clipEnd: Float,
        ): Long

        external fun getScale2(address: Long): Float
        external fun setScale2(address: Long, value: Float)
        external fun getOffset2(address: Long): Float
        external fun setOffset2(address: Long, value: Float)
    }
}
