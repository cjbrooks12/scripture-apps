package com.copperleaf.scripturenow.verses.mtg

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

interface ScryfallApi {

    @GET("cards/named")
    suspend fun getCardByName(
        @Query("fuzzy") fuzzy: String,
    ): ScryfallCardResponse

    @GET("cards/named")
    suspend fun getCardByNameAsJsonString(
        @Query("fuzzy") fuzzy: String,
    ): String

}

@Serializable
data class ScryfallCardResponse(
    val name: String = "",
    val mana_cost: String = "",
    val type_line: String = "",
    val oracle_text: String = "",
    val flavor_text: String = "",
    val power: String = "",
    val toughness: String = "",
    val keywords: List<String> = emptyList(),
    val legalities: Map<String, MtgLegality> = emptyMap(),
    val colors: List<@Serializable(with = SimpleColorSerializer::class) MtgColor> = emptyList(),
    val color_identity: List<@Serializable(with = SimpleColorSerializer::class) MtgColor> = emptyList(),
) {
    val legalForCommander: Boolean = legalities["commander"] == MtgLegality.legal

    override fun toString(): String {
        return """
            |$name         $mana_cost
            |
            |$type_line    
            |
            |${oracle_text.prependIndent("    ")}    
            |
            |${flavor_text.prependIndent("    ")}    
            |
            |$power/$toughness
            |
            |colors=$colors
            |color_identity=$color_identity
            |legalForCommander=$legalForCommander
        """.trimMargin()
            .lines()
            .let { lines ->
                val maxLineLength = lines.maxOf { it.length }

                buildString {
                    appendLine("+${"".padEnd(maxLineLength, padChar = '-')}+")
                    lines.forEach { line ->
                        appendLine("|${line.padEnd(maxLineLength)}|")
                    }
                    appendLine("+${"".padEnd(maxLineLength, padChar = '-')}+")
                }
            }
    }
}

object SimpleColorSerializer : KSerializer<MtgColor> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: MtgColor) {
        error("can only deserialize")
    }

    override fun deserialize(decoder: Decoder): MtgColor {
        return decoder.decodeString().scryfallColor
    }
}

private val String.scryfallColor: MtgColor
    get() = when (this) {
        "W" -> MtgColor.White
        "U" -> MtgColor.Blue
        "B" -> MtgColor.Black
        "R" -> MtgColor.Red
        "G" -> MtgColor.Green
        else -> error("unknown color")
    }

enum class MtgLegality {
    legal, not_legal, restricted, banned
}
