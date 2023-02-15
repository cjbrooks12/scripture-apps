package com.caseyjbrooks.scripturenow.utils

import com.caseyjbrooks.scripturenow.models.VerseReference
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

public class TestVerseUtils : StringSpec({
    "test parse" {
        "John 3:16".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..16)
        "John 3 :16".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..16)
        "John 3: 16".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..16)
        "John 3 : 16".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..16)

        "John 3:16-18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)
        "John 3 :16-18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)
        "John 3: 16-18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)
        "John 3 : 16-18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)

        "John 3:16 -18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)
        "John 3 :16 -18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)
        "John 3: 16 -18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)
        "John 3 : 16 -18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)

        "John 3:16- 18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)
        "John 3 :16- 18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)
        "John 3: 16- 18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)
        "John 3 : 16- 18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)

        "John 3:16 - 18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)
        "John 3 :16 - 18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)
        "John 3: 16 - 18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)
        "John 3 : 16 - 18".parseVerseReference() shouldBe VerseReference.KnownReference("John", 3, 16..18)

        "1 John 3:16".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..16)
        "1 John 3 :16".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..16)
        "1 John 3: 16".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..16)
        "1 John 3 : 16".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..16)

        "1 John 3:16-18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)
        "1 John 3 :16-18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)
        "1 John 3: 16-18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)
        "1 John 3 : 16-18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)

        "1 John 3:16 -18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)
        "1 John 3 :16 -18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)
        "1 John 3: 16 -18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)
        "1 John 3 : 16 -18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)

        "1 John 3:16- 18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)
        "1 John 3 :16- 18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)
        "1 John 3: 16- 18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)
        "1 John 3 : 16- 18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)

        "1 John 3:16 - 18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)
        "1 John 3 :16 - 18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)
        "1 John 3: 16 - 18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)
        "1 John 3 : 16 - 18".parseVerseReference() shouldBe VerseReference.KnownReference("1 John", 3, 16..18)
    }
    "test toString" {
        VerseReference.UnknownReference("John 3:16").referenceText shouldBe "John 3:16"
    }
})
