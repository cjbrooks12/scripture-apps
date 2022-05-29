package com.copperleaf.ballast.router

public sealed interface NavToken

public data class Tag(
    val tag: String,
) : NavToken

public data class Destination(
    val tag: Any,
    val path: String,
    val args: Map<String, NavArg<*>> = emptyMap(),
) : NavToken
