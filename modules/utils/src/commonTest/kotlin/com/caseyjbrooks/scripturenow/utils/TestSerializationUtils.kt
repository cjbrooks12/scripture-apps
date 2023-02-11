package com.caseyjbrooks.scripturenow.utils

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class DefaultContent(
    @SerialName(".a") val valueFromSerialName: String = "",
    @HtmlSelector(".a") val valueFromSelector: String = "",

    @SerialName(".b") val innerBFromSerialName: InnerDefaultContent = InnerDefaultContent(),
    @HtmlSelector(".b") val innerBFromSelector: InnerDefaultContent = InnerDefaultContent(),

    @SerialName(".c") val innerCFromSerialName: InnerDefaultContent = InnerDefaultContent(),
    @HtmlSelector(".c") val innerCFromSelector: InnerDefaultContent = InnerDefaultContent(),
)

@Serializable
public data class InnerDefaultContent(
    @SerialName(".inner") val innerValueFromSerialName: String = "",
    @HtmlSelector(".inner") val innerValueFromSelector: String = "",
)

@Serializable
public data class TagContent(
    @SerialName(".a") @HtmlContent val valueFromSerialName: String = "",
    @HtmlSelector(".a") @HtmlContent val valueFromSelector: String = "",

    @SerialName(".b") val innerBFromSerialName: InnerTagContent = InnerTagContent(),
    @HtmlSelector(".b") val innerBFromSelector: InnerTagContent = InnerTagContent(),

    @SerialName(".c") val innerCFromSerialName: InnerTagContent = InnerTagContent(),
    @HtmlSelector(".c") val innerCFromSelector: InnerTagContent = InnerTagContent(),
)

@Serializable
public data class InnerTagContent(
    @SerialName(".inner") @HtmlContent val innerValueFromSerialName: String = "",
    @HtmlSelector(".inner") @HtmlContent val innerValueFromSelector: String = "",
)

@Serializable
public data class AttributeValue(
    @SerialName(".a") @HtmlAttribute("value") val valueFromSerialName: String = "",
    @HtmlSelector(".a") @HtmlAttribute("value") val valueFromSelector: String = "",

    @SerialName(".b") val innerBFromSerialName: InnerAttributeContent = InnerAttributeContent(),
    @HtmlSelector(".b") val innerBFromSelector: InnerAttributeContent = InnerAttributeContent(),

    @SerialName(".c") val innerCFromSerialName: InnerAttributeContent = InnerAttributeContent(),
    @HtmlSelector(".c") val innerCFromSelector: InnerAttributeContent = InnerAttributeContent(),
)

@Serializable
public data class InnerAttributeContent(
    @SerialName(".inner") @HtmlAttribute("value") val innerValueFromSerialName: String = "",
    @HtmlSelector(".inner") @HtmlAttribute("value") val innerValueFromSelector: String = "",
)

public class TestSerializationUtils : StringSpec({
    "test parse" {
        val input = """
            <html lang="en">
              <head></head>
              <body>
                <div class="a" value="attribute value">tag value</div>
                <div class="b">
                  <div class="inner" value="inner attribute value (b)">inner tag value (b)</div>
                </div>
                <div class="c">
                  <div class="inner" value="inner attribute value (c)">inner tag value (c)</div>
                </div>
              </body>
            </html>
        """.trimIndent()

        val defaultContent = Html().decodeFromString(DefaultContent.serializer(), input)
        defaultContent.valueFromSerialName shouldBe "tag value"
        defaultContent.valueFromSelector shouldBe "tag value"
        defaultContent.innerBFromSerialName.innerValueFromSerialName shouldBe "inner tag value (b)"
        defaultContent.innerBFromSelector.innerValueFromSelector shouldBe "inner tag value (b)"
        defaultContent.innerCFromSerialName.innerValueFromSerialName shouldBe "inner tag value (c)"
        defaultContent.innerCFromSelector.innerValueFromSelector shouldBe "inner tag value (c)"

        val tagContent = Html().decodeFromString(TagContent.serializer(), input)
        tagContent.valueFromSerialName shouldBe "tag value"
        tagContent.valueFromSelector shouldBe "tag value"
        tagContent.innerBFromSerialName.innerValueFromSerialName shouldBe "inner tag value (b)"
        tagContent.innerBFromSelector.innerValueFromSelector shouldBe "inner tag value (b)"
        tagContent.innerCFromSerialName.innerValueFromSerialName shouldBe "inner tag value (c)"
        tagContent.innerCFromSelector.innerValueFromSelector shouldBe "inner tag value (c)"

        val attributeValue = Html().decodeFromString(AttributeValue.serializer(), input)
        attributeValue.valueFromSerialName shouldBe "attribute value"
        attributeValue.valueFromSelector shouldBe "attribute value"
        attributeValue.innerBFromSerialName.innerValueFromSerialName shouldBe "inner attribute value (b)"
        attributeValue.innerBFromSelector.innerValueFromSelector shouldBe "inner attribute value (b)"
        attributeValue.innerCFromSerialName.innerValueFromSerialName shouldBe "inner attribute value (c)"
        attributeValue.innerCFromSelector.innerValueFromSelector shouldBe "inner attribute value (c)"
    }
})
