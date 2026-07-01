# ============================================
# Gestor360 ADMIN
# ProGuard / R8 Rules
# ============================================

##############################################
# Kotlin Metadata
##############################################
-keep class kotlin.Metadata { *; }

##############################################
# Koin
##############################################
-keep class org.koin.** { *; }
-dontwarn org.koin.**

##############################################
# Room
##############################################
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# Mantener entidades y DAOs
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }

##############################################
# Supabase Kotlin SDK
##############################################
-keep class io.github.jan.supabase.** { *; }
-dontwarn io.github.jan.supabase.**

##############################################
# Ktor
##############################################
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

##############################################
# Kotlinx Serialization
##############################################
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable *;
}

##############################################
# Coroutines
##############################################
-dontwarn kotlinx.coroutines.**

##############################################
# Mantener clases de la aplicación
##############################################
-keep class com.gestor360.admin.** { *; }

##############################################
# Eliminar mensajes de advertencia comunes
##############################################
-dontwarn kotlin.**
-dontwarn org.jetbrains.annotations.**
