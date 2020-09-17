package com.sergstas.lib.extensions

import kotlin.math.pow
import kotlin.math.roundToInt

public fun Double.round(pos: Int): Double {
    return (this * 10.0.pow(pos)).roundToInt() / 10.0.pow(pos)
}