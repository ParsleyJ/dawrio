package com.parsleyj.dawrio.daw.device

import com.parsleyj.dawrio.daw.ValueFormat

class ChannelDescriptor(
    val valueFormat: ValueFormat = ValueFormat.NumericWithDecimals(1),
    val name: String? = null,
)