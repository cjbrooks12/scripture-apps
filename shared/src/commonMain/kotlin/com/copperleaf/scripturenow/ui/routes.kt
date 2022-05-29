package com.copperleaf.scripturenow.ui

import com.copperleaf.ballast.router.Destination
import com.copperleaf.ballast.router.NavArg
import com.copperleaf.ballast.router.RouterContract

sealed class Route(protected val path: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Route) return false

        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }

    open fun navArgs(): Map<String, NavArg<*>> = emptyMap()
    fun destination(): Destination = Destination(
        this,
        navArgs().entries.fold(path) { acc, nextEntry ->
            acc.replace("{${nextEntry.key}}", nextEntry.value.value.toString())
        },
        navArgs(),
    )
}

object Destinations {
    object App {
        object Home : Route("/app/home")

        object VerseOfTheDay : Route("/app/votd")

        object Verses : Route("/app/verses")
        data class ViewVerse(val verseId: Int) : Route("/app/verses/{verseId}") {
            override fun navArgs(): Map<String, NavArg<*>> = mapOf(
                "verseId" to NavArg.IntArg(verseId)
            )
        }

        data class EditVerse(val verseId: Int) : Route("/app/verses/{verseId}/edit") {
            override fun navArgs(): Map<String, NavArg<*>> = mapOf(
                "verseId" to NavArg.IntArg(verseId)
            )
        }
    }
}

val RouterContract.State.currentDestination: Destination?
    get() {
        return backstack.lastOrNull() as? Destination
    }
val Destination.currentRoute: Route
    get() {
        return this.tag as Route
    }
