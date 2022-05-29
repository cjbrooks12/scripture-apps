package com.copperleaf.scripturenow.verses.mtg

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MtgInputCsv(
    @SerialName("Quantity")
    val quantity: String,
    @SerialName("Name")
    val name: String,
    @SerialName("CardNumber")
    val cardNumber: String,
    @SerialName("Expansion Code")
    val expansionCode: String,
    @SerialName("Expansion Name")
    val expansionName: String,
    @SerialName("PurchasePrice")
    val purchasePrice: String,
    @SerialName("Foil")
    val foil: String,
    @SerialName("Condition")
    val condition: String,
    @SerialName("Language")
    val language: String,
    @SerialName("PurchaseDate")
    val purchaseDate: String,
    @SerialName("Single Current Price")
    val singleCurrentPrice: String,
    @SerialName("Total Current Price")
    val totalCurrentPrice: String,
)

@Serializable
data class MtgOutputCsv(
    // original columns
    @SerialName("Quantity")
    val quantity: String,
    @SerialName("Name")
    val name: String,
    @SerialName("CardNumber")
    val cardNumber: String,
    @SerialName("Expansion Code")
    val expansionCode: String,
    @SerialName("Expansion Name")
    val expansionName: String,
    @SerialName("PurchasePrice")
    val purchasePrice: String,
    @SerialName("Foil")
    val foil: String,
    @SerialName("Condition")
    val condition: String,
    @SerialName("Language")
    val language: String,
    @SerialName("PurchaseDate")
    val purchaseDate: String,
    @SerialName("Single Current Price")
    val singleCurrentPrice: String,
    @SerialName("Total Current Price")
    val totalCurrentPrice: String,

    // new columns
    @SerialName("Mana Cost")
    val mana_cost: String = "",
    @SerialName("Type")
    val type_line: String = "",
    @SerialName("Oracle Text")
    val oracle_text: String = "",
    @SerialName("Power")
    val power: String = "",
    @SerialName("Toughness")
    val toughness: String = "",
    @SerialName("Card Colors")
    val colors: String = "",
    @SerialName("Color Identity")
    val color_identity: String = "",
    @SerialName("Legal In Commander")
    val legal: String = "",
)
