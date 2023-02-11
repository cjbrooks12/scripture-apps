package com.caseyjbrooks.scripturenow.utils

public val ClosedRange<Int>.size: Int get() = (endInclusive - start) + 1
