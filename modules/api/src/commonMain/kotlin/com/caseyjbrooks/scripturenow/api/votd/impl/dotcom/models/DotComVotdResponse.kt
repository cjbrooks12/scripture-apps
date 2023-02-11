package com.caseyjbrooks.scripturenow.api.votd.impl.dotcom.models

import com.caseyjbrooks.scripturenow.utils.HtmlAttribute
import com.caseyjbrooks.scripturenow.utils.HtmlContent
import com.caseyjbrooks.scripturenow.utils.HtmlSelector
import kotlinx.serialization.Serializable

@Serializable
public data class DotComVotdResponse(
    @HtmlSelector("meta[property='og:description']") @HtmlAttribute("content") val ogDescription: String = "",
    @HtmlSelector("meta[property='og:url']") @HtmlAttribute("content") val ogUrl: String = "",
    @HtmlSelector(".scripture .reference > a") @HtmlContent val reference: String = "",
)
