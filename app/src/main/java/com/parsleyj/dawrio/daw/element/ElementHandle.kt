package com.parsleyj.dawrio.daw.element

@JvmInline
value class ElementHandle(val toAddress: Long)

fun Long.toElementHandle(): ElementHandle {
    return ElementHandle(this)
}