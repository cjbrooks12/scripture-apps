package com.caseyjbrooks.scripturenow.models.routing

import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher

public enum class ScriptureNowRoute(
    routeFormat: String,
    override val annotations: Set<RouteAnnotation> = emptySet(),
) : Route {
    Home("/app/home"),

    VerseOfTheDay("/app/votd"),
    Settings("/app/settings"),

    MemoryVerseList("/app/verses"),
    MemoryVerseDetails("/app/verses/{verseId}"),
    MemoryVerseCreate("/app/verses/new"),
    MemoryVerseEdit("/app/verses/{verseId}/edit"),

    PrayerList("/app/prayers"),
    PrayerDetails("/app/prayers/{prayerId}"),
    PrayerCreate("/app/prayers/new"),
    PrayerEdit("/app/prayers/{prayerId}/edit");

    override val matcher: RouteMatcher = RouteMatcher.create(routeFormat)
}
