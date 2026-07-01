package com.admin360.feature.dashboard.data

import com.admin360.core.supabase.SupabaseModule
import com.admin360.feature.dashboard.model.DashboardDto
import io.github.jan.supabase.postgrest.from

class DashboardRepository {

    private val db = SupabaseModule.client

    suspend fun getDashboard(): DashboardDto {

        val negocios = db.from("negocios").select().decodeList<Any>().size
        val locales = db.from("locales").select().decodeList<Any>().size
        val usuarios = db.from("usuarios").select().decodeList<Any>().size

        val licActivas = db.from("licencias")
            .select {
                filter { eq("estado", "ACTIVA") }
            }.decodeList<Any>().size

        val licVencidas = db.from("licencias")
            .select {
                filter { eq("estado", "VENCIDA") }
            }.decodeList<Any>().size

        return DashboardDto(
            totalNegocios = negocios,
            totalLocales = locales,
            totalUsuarios = usuarios,
            licenciasActivas = licActivas,
            licenciasVencidas = licVencidas
        )
    }
}
