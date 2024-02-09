package com.caseyjbrooks.prayer.repository

import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.repository.saved.FakeSavedPrayersRepository
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import com.caseyjbrooks.prayer.utils.PrayerId
import com.caseyjbrooks.prayer.utils.getPrayer
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

public class FakeSavedPrayersRepositoryTest : StringSpec({
    "Basic happy-path CRUD operations" {
        val repo: SavedPrayersRepository = FakeSavedPrayersRepository()

        // check initial state
        repo.getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet(), null).first() shouldBe emptyList()

        // add some prayers to the DB
        repo.createPrayer(getPrayer(1, "prayer one"))
        repo.createPrayer(getPrayer(2, "prayer two"))

        // check they got saved
        repo.getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet(), null).first() shouldBe listOf(
            getPrayer(1, "prayer one"),
            getPrayer(2, "prayer two"),
        )

        // check doing some basic queries
        repo.getPrayerById(PrayerId(1)).first() shouldBe getPrayer(1, "prayer one")
        repo.getPrayerById(PrayerId(2)).first() shouldBe getPrayer(2, "prayer two")
        repo.getPrayerById(PrayerId(3)).first().shouldBeNull()

        repo.getPrayerByText("prayer one").first() shouldBe getPrayer(1, "prayer one")
        repo.getPrayerByText("prayer two").first() shouldBe getPrayer(2, "prayer two")
        repo.getPrayerByText("prayer three").first().shouldBeNull()

        // update one prayer, and delete the other
        repo.deletePrayer(getPrayer(1, "prayer one"))
        repo.updatePrayer(getPrayer(2, "Updated prayer two text"))

        // check those changes were also saved
        repo.getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet(), null).first() shouldBe listOf(
            getPrayer(2, "Updated prayer two text"),
        )
        repo.getPrayerById(PrayerId(2)).first() shouldBe getPrayer(2, "Updated prayer two text")
        repo.getPrayerByText("prayer two").first().shouldBeNull()
        repo.getPrayerByText("Updated prayer two text").first() shouldBe getPrayer(2, "Updated prayer two text")
    }

    "Create prayer - prayer already exists -> fails" {
        val repo: SavedPrayersRepository = FakeSavedPrayersRepository()

        // adding the prayer the first time succeeds
        shouldNotThrowAny {
            repo.createPrayer(getPrayer(1, "prayer one"))
        }

        // trying to add the same prayer again should fail
        shouldThrowAny {
            repo.createPrayer(getPrayer(1, "prayer one"))
        }
    }

    "Update prayer - prayer not found -> fails" {
        val repo: SavedPrayersRepository = FakeSavedPrayersRepository()

        repo.createPrayer(getPrayer(1, "prayer one"))

        // trying to update a prayer with an ID not currently in the DB should fail
        shouldThrowAny {
            repo.updatePrayer(getPrayer(2, "prayer two"))
        }
    }

    "Delete prayer - prayer not found -> fails" {
        val repo: SavedPrayersRepository = FakeSavedPrayersRepository()

        repo.createPrayer(getPrayer(1, "prayer one"))

        // trying to delete a prayer with an ID not currently in the DB should fail
        shouldThrowAny {
            repo.deletePrayer(getPrayer(2, "prayer two"))
        }
    }
})
