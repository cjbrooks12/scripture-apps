package com.caseyjbrooks.scripturenow.models.votd

public enum class VerseOfTheDayService(
    public val todaySupported: Boolean,
    public val historicalVersesSupported: Boolean,
    public val randomVerseSupported: Boolean,
) {
    VerseOfTheDayDotCom(true, true, false),
    BibleGateway(true, false, false),
    OurManna(true, false, true),
    TheySaidSo(true, false, true),
}
