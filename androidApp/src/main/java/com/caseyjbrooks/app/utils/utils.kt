package com.caseyjbrooks.app.utils

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.coroutines.resume
import kotlin.reflect.KClass


/**
 * Add .exhaustive to a `when()` statement to force it into being an expression, which requires all
 * branches to be handled and return a value.
 */
inline val <T> T.exhaustive get() = this

/**
 * Compose Snackbars have a weird API which suspends until the snackbar is dismissed. This will
 * block the normal dispatching of events, despite really only being a decorative UI element, and
 * there's no real strong reason to suspend the event handler queue for a snackbar.
 *
 * With respect to launching it in the activity.lifecycleScope rather than getting a reference to
 * the viewModelScope in the Event handler, it is a UI element and is better-off being tied to the
 * View's lifecycle rather than the ViewModel's.
 */
fun postSnackbar(
    activity: NavGraphActivity,
    snackbarHostState: SnackbarHostState,
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onActionClicked: (suspend () -> Unit)? = null,
) {
    activity.lifecycleScope.launch {
        snackbarHostState.currentSnackbarData?.dismiss()
        val result = snackbarHostState.showSnackbar(
            message,
            actionLabel = actionLabel,
            duration = duration,
        )
        if (result == SnackbarResult.ActionPerformed
            && actionLabel != null
            && onActionClicked != null
        ) {
            onActionClicked()
        }
    }
}

fun openEmail(
    activity: AppCompatActivity,
    emailAddress: String,
) {
    runCatching {
        activity.startActivity(
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$emailAddress")
            }
        )
    }.onFailure {
        Log.e("openEmail", "", it)
    }
}

fun openPlayStore(
    activity: AppCompatActivity,
) {
    runCatching {
        activity.startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=${activity.packageName}")
            }
        )
    }.onFailure {
        Log.e("openPlayStore", "", it)
    }
}

fun Modifier.thenIf(
    applyModifier: Boolean,
    optionalModifier: () -> Modifier,
): Modifier {
    return this.then(
        if (applyModifier) {
            optionalModifier()
        } else {
            Modifier
        }
    )
}

fun <T : Any> Modifier.thenIfNotNull(
    optionalValue: T?,
    enabled: Boolean = true,
    optionalModifier: (T) -> Modifier,
): Modifier {
    return this.then(
        if (optionalValue != null && enabled) {
            optionalModifier(optionalValue)
        } else {
            Modifier
        }
    )
}

fun <T, U> Flow<List<T>>.mapEachItem(mapper: (T) -> U): Flow<List<U>> {
    return this.map { list ->
        list.map { item ->
            mapper(item)
        }
    }
}

fun <T> Flow<Result<T>>.filterSuccessfulResult(): Flow<T> = this
    .filter { it.isSuccess }
    .map { it.getOrThrow() }

suspend fun <T> Flow<Result<T>>.firstSuccessfulResult(): T? = this
    .filter { it.isSuccess }
    .map { it.getOrThrow() }
    .firstOrNull()

/**
 * Wrap the display and selection of a DatePickerDialog in a coroutine. When a user successfully
 * selects a data and hits "OK" in the dialog, this method returns with that new date, When a user
 * dismisses the dialog or hits "Cancel", this method returns null.
 */
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun awaitDateSelection(
    activity: AppCompatActivity,
    initialDate: LocalDate,
    minDate: LocalDateTime? = null,
    maxDate: LocalDateTime? = null,
): LocalDate? {
    return suspendCancellableCoroutine { cont ->
        var selectedDate: LocalDate? = null
        DatePickerDialog(
            activity,
            { _, year, month, day ->
                selectedDate = LocalDate.of(year, month + 1, day)
            },
            initialDate.year,
            initialDate.monthValue - 1,
            initialDate.dayOfMonth
        ).apply {
            setOnDismissListener { cont.resume(selectedDate) }
            minDate?.let {
                datePicker.minDate = it.toInstant(ZoneOffset.UTC).toEpochMilli()
            }
            maxDate?.let {
                datePicker.maxDate = it.toInstant(ZoneOffset.UTC).toEpochMilli()
            }
        }.show()
    }
}

fun <T : Any, Ann : Annotation> T.isAnnotatedWith(annotationClass: KClass<Ann>): Boolean {
    return this::class.java.isAnnotationPresent(annotationClass.java)
}

// Firebase
// ---------------------------------------------------------------------------------------------------------------------

interface AsyncProvider<T : Any> {
    suspend fun get(): T
}

class LazyAsyncProvider<T : Any>(
    val provider: suspend () -> T
) : AsyncProvider<T> {
    private var fetched: Boolean = false
    private var value: T? = null

    override suspend fun get(): T {
        if (!fetched) {
            value = provider()
        }
        return checkNotNull(value) {
            "There was an error providing async value"
        }
    }
}

//suspend fun <T : Any> Task<T>.await(): T? {
//    return suspendCancellableCoroutine { cont ->
//        this.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                cont.resume(task.result)
//            } else {
//                cont.resume(null)
//            }
//        }
//    }
//}
//
//enum class RemoteConfigFetchResult {
//    FetchedAndUpdated, FetchedWithNoChange, Failure
//}
//
//suspend fun FirebaseRemoteConfig.awaitFetch(): RemoteConfigFetchResult {
//    val result = this
//        .fetchAndActivate()
//        .await()
//
//    return if (result != null) {
//        if (result) {
//            RemoteConfigFetchResult.FetchedAndUpdated
//        } else {
//            RemoteConfigFetchResult.FetchedWithNoChange
//        }
//    } else {
//        RemoteConfigFetchResult.Failure
//    }
//}
