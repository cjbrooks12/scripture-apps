package com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlCData
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("rss")
public data class BibleGatewayVotdResponse(
    @XmlElement(true) val channel: Channel = Channel(),
) {
    @Serializable
    @SerialName("channel")
    public data class Channel(
        @XmlElement(true) val item: Item = Item(),
    ) {
        @Serializable
        @SerialName("item")
        public data class Item(
            @XmlElement(true) @SerialName("title") val title: String = "",
            @XmlElement(true) @SerialName("guid") val guid: String = "",
            @XmlCData(true) @XmlSerialName(value = "encoded", namespace = "http://purl.org/rss/1.0/modules/content/", prefix = "content") val content: String = "",
            @XmlElement(true) @XmlSerialName(value = "rights", namespace = "http://purl.org/dc/elements/1.1/", prefix = "dc") val rights: String = "",
        )
    }
}
