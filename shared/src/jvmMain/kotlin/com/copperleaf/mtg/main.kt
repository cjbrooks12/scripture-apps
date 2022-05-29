package com.copperleaf.mtg

import com.copperleaf.scripturenow.verses.mtg.MtgInputCsv
import com.copperleaf.scripturenow.verses.mtg.MtgOutputCsv
import com.copperleaf.scripturenow.verses.mtg.ScryfallApi
import com.copperleaf.scripturenow.verses.mtg.ScryfallCardResponse
import com.copperleaf.scripturenow.verses.mtg._ScryfallApiImpl
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.internal.KtorfitClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.system.exitProcess

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    runBlocking {
        val baseDir = File("./shared/src/jvmMain/resources")
        val cacheDirectory = baseDir.resolve("cache")
        val scryfallCacheDirectory = cacheDirectory.resolve("scryfall")
        val inputFile = baseDir.resolve("input/input.csv")

        val json = Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }

        val httpClient = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(json)
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.BODY
            }
        }
        val scryfallApi: ScryfallApi = _ScryfallApiImpl(
            KtorfitClient(
                Ktorfit(
                    "https://api.scryfall.com/",
                    httpClient,
                )
            )
        )

        val csv = Csv(
            CsvConfiguration(
                hasHeaderRecord = true,
                hasTrailingDelimiter = true,
                recordSeparator = "\n"
            )
        )

        val input = inputFile.readText()
        val parsed = csv.decodeFromString(ListSerializer(MtgInputCsv.serializer()), input)

        parsed
            .asFlow()
            .withIndex()
            .map { (index, input) ->
                val safeFileName = input.name.replace("\\W".toRegex(), "_")
                val cachedFile = scryfallCacheDirectory.resolve("${safeFileName}.json")

                val apiResponseJson = if (!cachedFile.exists()) {
                    delay(1000)
                    println("Fetching '${input.name}' (${index + 1}/${parsed.size})")
                    println("--------------------------------------------------------------------------------")
                    scryfallApi.getCardByNameAsJsonString(input.name).also {
                        cachedFile.writeText(it)
                    }
                } else {
                    println("Using cached '${input.name}' (${index + 1}/${parsed.size})")
                    cachedFile.readText()
                }

                val apiResponse = json.decodeFromString(ScryfallCardResponse.serializer(), apiResponseJson)

                MtgOutputCsv(
                    quantity = input.quantity,
                    name = input.name,
                    cardNumber = input.cardNumber,
                    expansionCode = input.expansionCode,
                    expansionName = input.expansionName,
                    purchasePrice = input.purchasePrice,
                    foil = input.foil,
                    condition = input.condition,
                    language = input.language,
                    purchaseDate = input.purchaseDate,
                    singleCurrentPrice = input.singleCurrentPrice,
                    totalCurrentPrice = input.totalCurrentPrice,

                    // new columns
                    mana_cost = apiResponse.mana_cost,
                    type_line = apiResponse.type_line,
                    oracle_text = apiResponse.oracle_text,
                    power = apiResponse.power,
                    toughness = apiResponse.toughness,
                    colors = apiResponse.colors.joinToString { it.name },
                    color_identity = apiResponse.color_identity.joinToString { it.name },
                    legal = if (apiResponse.legalForCommander) "true" else "false",
                ).also {
                    println("--------------------------------------------------------------------------------")
                }
            }
            .toList()
            .let { remapped ->
                val outputFile = baseDir.resolve("output/output.csv")
                if (outputFile.exists()) {
                    outputFile.delete()
                }
                outputFile.createNewFile()
                outputFile.writeText(
                    csv.encodeToString(ListSerializer(MtgOutputCsv.serializer()), remapped)
                )
            }


        parsed
            .groupBy { it.name }
            .let { groupedValues ->
                val outputDecklistText = baseDir.resolve("output/decklist.txt")
                if (outputDecklistText.exists()) {
                    outputDecklistText.delete()
                }
                outputDecklistText.createNewFile()
                outputDecklistText.writeText(
                    buildString {
                        groupedValues.forEach { (name, values) ->
                            appendLine("${values.size}x $name")
                        }
                    }
                )
            }
    }

    exitProcess(0)
}
