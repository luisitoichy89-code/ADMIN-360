# Supabase
-keep class io.github.jan.supabase.** { *; }

# Kotlin Serialization
-keepattributes *Annotation*
-dontwarn kotlinx.serialization.**

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel { *; }
