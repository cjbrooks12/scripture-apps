package com.copperleaf.ballast.router.routing

public sealed interface NavArg<T : Any> {
    public val value: T

    public data class StringArg(override val value: String) : NavArg<String>
    public data class IntArg(override val value: Int) : NavArg<Int>
    public data class LongArg(override val value: Long) : NavArg<Long>
    public data class FloatArg(override val value: Float) : NavArg<Float>
    public data class DoubleArg(override val value: Double) : NavArg<Double>
    public data class BooleanArg(override val value: Boolean) : NavArg<Boolean>
}
