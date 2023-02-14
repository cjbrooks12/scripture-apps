package com.caseyjbrooks.scripturenow.utils.converter

public fun interface Converter<From, To> {
    public fun convertValue(from: From): To
}
