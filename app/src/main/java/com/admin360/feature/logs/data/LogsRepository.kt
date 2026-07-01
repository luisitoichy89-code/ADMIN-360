package com.admin360.feature.logs.data

import com.admin360.core.supabase.SupabaseModule
import com.admin360.feature.logs.model.LogDto
import io.github.jan.supabase.postgrest.from

class LogsRepository {

    private val db = SupabaseModule.client.from("logs")

    suspend fun getRecent(): List<LogDto> {
        return db.select()
            .decodeList()
    }
}
