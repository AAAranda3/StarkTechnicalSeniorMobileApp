
# Retain line numbers and source file names for crash analysis / bug reporting
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Retain generic signatures and runtime annotations
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Flatten obfuscated packages into a root package to prevent package hierarchy exposure
-repackageclasses 'com.stark.varg.obfuscated'
-allowaccessmodification


# Prevent Moshi generated adapters and models from being stripped or fields renamed
-keepclasseswithmembers class * {
    @com.squareup.moshi.Json <fields>;
}

-keep @interface com.squareup.moshi.*
-keep @com.squareup.moshi.JsonQualifier @interface *

-keep @com.squareup.moshi.JsonClass class * {
    <init>(...);
    <fields>;
}

# Preserve Stark Telemetry DTOs and their generated KSP adapters
-keep class com.stark.varg.data.dto.** { *; }
-keep class com.stark.varg.data.dto.**JsonAdapter {
    <init>(com.squareup.moshi.Moshi);
    *;
}

-dontwarn com.squareup.moshi.**

-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keep class * extends androidx.lifecycle.ViewModelProvider$Factory { *; }
-keep,allowobfuscation,allowshrinking @dagger.hilt.android.lifecycle.HiltViewModel class *

-dontwarn dagger.hilt.**
-dontwarn javax.annotation.**

-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}


-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

-keep class androidx.datastore.** { *; }
-dontwarn androidx.datastore.**

-keep class coil.** { *; }
-dontwarn coil.**