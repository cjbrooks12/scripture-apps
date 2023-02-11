-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile

-dontwarn com.google.firebase.messaging.TopicOperation$TopicOperations
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.impl.StaticMDCBinder

# Fixes a crash in R8
-keep class nl.adaptivity.** { *; }
-dontwarn nl.adaptivity.**
