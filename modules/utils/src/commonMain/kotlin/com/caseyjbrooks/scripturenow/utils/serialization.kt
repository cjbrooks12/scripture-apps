@file:OptIn(ExperimentalSerializationApi::class)
package com.caseyjbrooks.scripturenow.utils

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
public annotation class HtmlSelector(
    val selector: String,
)

@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
public annotation class HtmlContent

@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
public annotation class HtmlAttribute(
    val attributeName: String = "",
)

private sealed interface HtmlValue {

    val selector: String
    fun getSelectedElements(elements: Elements): Elements
    fun getValue(elements: Elements): Any

    class AttributeValue(
        override val selector: String,
        val attributeName: String = "",
    ) : HtmlValue {
        override fun getSelectedElements(elements: Elements): Elements {
            return elements.select(selector)
        }

        override fun getValue(elements: Elements): Any {
            return getSelectedElements(elements).attr(attributeName)
        }
    }

    class ContentValue(
        override val selector: String,
    ) : HtmlValue {
        override fun getSelectedElements(elements: Elements): Elements {
            return elements.select(selector)
        }

        override fun getValue(elements: Elements): Any {
            return getSelectedElements(elements).text()
        }
    }

    class ElementValue(
        override val selector: String,
    ) : HtmlValue {
        override fun getSelectedElements(elements: Elements): Elements {
            return elements.select(selector)
        }

        override fun getValue(elements: Elements): Any {
            error("no value to select")
        }
    }
}

public class Html(
    override val serializersModule: SerializersModule = EmptySerializersModule()
) : StringFormat {

    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
        val document = Jsoup.parse(string)
        val decoder = JsoupDocumentDecoder(document, serializersModule)
        return deserializer.deserialize(decoder)
    }

    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
        error("cannot encode to HTML format")
    }
}

private class JsoupDocumentDecoder(
    private val document: Document,
    override val serializersModule: SerializersModule,
) : AbstractDecoder() {
    override fun decodeValue(): Any {
        error("Cannot decode a value directly from the document")
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        return 0
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return JsoupNodeDecoder(
            selector = null,
            elements = document.allElements,
            serializersModule = serializersModule,
        )
    }
}

private class JsoupNodeDecoder(
    private val selector: String?,
    private val elements: Elements,
    override val serializersModule: SerializersModule,
) : AbstractDecoder() {
    private var elementIndex = 0
    private var currentDescriptor: SerialDescriptor? = null

    override fun decodeValue(): Any {
        val htmlValue = getHtmlValue(
            currentDescriptor!!.getElementName(elementIndex - 1),
            currentDescriptor!!.getElementAnnotations(elementIndex - 1),
        ) { HtmlValue.ContentValue(it) }
        return htmlValue.getValue(this.elements)
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (elementIndex == descriptor.elementsCount) return CompositeDecoder.DECODE_DONE
        currentDescriptor = descriptor
        return elementIndex++
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        val htmlValue = getHtmlValue(
            currentDescriptor!!.getElementName(elementIndex - 1),
            currentDescriptor!!.getElementAnnotations(elementIndex - 1),
        ) { HtmlValue.ContentValue(it) }

        return JsoupNodeDecoder(
            selector = htmlValue.selector,
            elements = htmlValue.getSelectedElements(elements),
            serializersModule = serializersModule,
        )
    }

    private fun getHtmlValue(
        elementName: String,
        annotations: List<Annotation>,
        defaultValue: (String) -> HtmlValue,
    ): HtmlValue {
        val selector = annotations.filterIsInstance<HtmlSelector>().firstOrNull()?.selector
            ?: annotations.filterIsInstance<SerialName>().firstOrNull()?.value
            ?: elementName

        val attributeName = annotations.filterIsInstance<HtmlAttribute>().firstOrNull()?.attributeName
        val forceTagContent = annotations.filterIsInstance<HtmlContent>().firstOrNull()

        return if (attributeName != null) {
            HtmlValue.AttributeValue(selector, attributeName)
        } else if (forceTagContent != null) {
            HtmlValue.ContentValue(selector)
        } else {
            defaultValue(selector)
        }
    }
}

public fun Configuration.html(
    format: Html = Html(),
    contentType: ContentType = ContentType.Text.Html
) {
    serialization(contentType, format)
}
