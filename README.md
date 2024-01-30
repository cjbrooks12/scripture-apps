# Scripture Now - Android

## App Libraries to know about

The following lists the main libraries/techniques being used for this app implementation:

- [Kotlin](https://kotlinlang.org/docs/home.html)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Kotlinx Serialization](https://kotlinlang.org/docs/serialization.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose/documentation)
- [SQLDelight](https://cashapp.github.io/sqldelight/android_sqlite/)
- [Ktorfit](https://github.com/Foso/Ktorfit)
- [Ballast](https://github.com/copper-leaf/ballast)
- [Ballast Navigation](https://copper-leaf.github.io/ballast/wiki/modules/ballast-navigation/)

Enable Firebase Analytics DebugView with the following command:

```shell
adb shell setprop debug.firebase.analytics.app com.caseybrooks.scripturememory.dev
adb shell setprop debug.firebase.analytics.app com.caseybrooks.openbible.dev
```

## Uploading builds to Firebase
