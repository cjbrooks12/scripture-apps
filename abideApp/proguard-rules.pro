-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile

-dontwarn com.google.firebase.messaging.TopicOperation$TopicOperations
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.impl.StaticMDCBinder

# Fixes a crash in R8
-keep class nl.adaptivity.** { *; }
-dontwarn nl.adaptivity.**

# Keep classes created by reflection
-keep class com.caseyjbrooks.prayer.schedules.PrayerSchedulerAdapter
-keep class com.caseyjbrooks.prayer.schedules.PrayerSchedulerCallback
-keep class com.caseyjbrooks.votd.schedules.VotdSchedulerAdapter
-keep class com.caseyjbrooks.votd.schedules.VotdSchedulerCallback
-keep class com.caseyjbrooks.database.ScriptureNowDatabase
