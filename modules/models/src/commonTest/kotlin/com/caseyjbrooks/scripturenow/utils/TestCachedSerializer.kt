package com.caseyjbrooks.scripturenow.utils

import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

public class TestCachedSerializer : StringSpec({
    "test parse" {
        val serializer = CachedSerializer(String.serializer())
        val a = Cached.NotLoaded<String>(null)
        val b = Cached.NotLoaded<String>("one")
        val c = Cached.Fetching<String>(null)
        val d = Cached.Fetching<String>("one")
        val e = Cached.FetchingFailed<String>(RuntimeException("test error"), null)
        val f = Cached.FetchingFailed<String>(RuntimeException("test error"), "one")
        val g = Cached.Value<String>("one")

        a.toString() shouldBe "NotLoaded(previousCachedValue=null)"
        b.toString() shouldBe "NotLoaded(previousCachedValue=one)"
        c.toString() shouldBe "Fetching(cachedValue=null)"
        d.toString() shouldBe "Fetching(cachedValue=one)"
        e.toString() shouldBe "FetchingFailed(error=test error, cachedValue=null)"
        f.toString() shouldBe "FetchingFailed(error=test error, cachedValue=one)"
        g.toString() shouldBe "Value(value=one)"

        val aEncoded = Json.encodeToString(serializer, a)
        val bEncoded = Json.encodeToString(serializer, b)
        val cEncoded = Json.encodeToString(serializer, c)
        val dEncoded = Json.encodeToString(serializer, d)
        val eEncoded = Json.encodeToString(serializer, e)
        val fEncoded = Json.encodeToString(serializer, f)
        val gEncoded = Json.encodeToString(serializer, g)

        aEncoded shouldBe """ {"state":"NotLoaded"} """.trim()
        bEncoded shouldBe """ {"state":"NotLoaded","value":"one"} """.trim()
        cEncoded shouldBe """ {"state":"Fetching"} """.trim()
        dEncoded shouldBe """ {"state":"Fetching","value":"one"} """.trim()
        eEncoded shouldBe """ {"state":"FetchingFailed"} """.trim()
        fEncoded shouldBe """ {"state":"FetchingFailed","value":"one"} """.trim()
        gEncoded shouldBe """ {"state":"Value","value":"one"} """.trim()

        val aDecoded = Json.decodeFromString(serializer, aEncoded)
        val bDecoded = Json.decodeFromString(serializer, bEncoded)
        val cDecoded = Json.decodeFromString(serializer, cEncoded)
        val dDecoded = Json.decodeFromString(serializer, dEncoded)
        val eDecoded = Json.decodeFromString(serializer, eEncoded)
        val fDecoded = Json.decodeFromString(serializer, fEncoded)
        val gDecoded = Json.decodeFromString(serializer, gEncoded)

        infix fun <T : Any> Cached<T>.isEquivalentTo(other: Cached<T>) {
            this::class shouldBe other::class
            this.getCachedOrNull() shouldBe other.getCachedOrNull()
        }

        aDecoded isEquivalentTo a
        bDecoded isEquivalentTo b
        cDecoded isEquivalentTo c
        dDecoded isEquivalentTo d
        eDecoded isEquivalentTo e
        fDecoded isEquivalentTo f
        gDecoded isEquivalentTo g

        aDecoded shouldNotBeSameInstanceAs a
        bDecoded shouldNotBeSameInstanceAs b
        cDecoded shouldNotBeSameInstanceAs c
        dDecoded shouldNotBeSameInstanceAs d
        eDecoded shouldNotBeSameInstanceAs e
        fDecoded shouldNotBeSameInstanceAs f
        gDecoded shouldNotBeSameInstanceAs g
    }
})
