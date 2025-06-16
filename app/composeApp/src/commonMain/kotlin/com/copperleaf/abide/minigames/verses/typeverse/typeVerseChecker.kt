package com.copperleaf.abide.minigames.verses.typeverse


sealed interface TypeVerseMinigameResult {
    data object Perfect : TypeVerseMinigameResult
    data object Partial : TypeVerseMinigameResult
    data object Failed : TypeVerseMinigameResult
    data object Cancelled : TypeVerseMinigameResult
}

fun checkVerse(
    typedText: String,
    verseText: String,
) : TypeVerseMinigameResult {
    return TypeVerseMinigameResult.Perfect
}
