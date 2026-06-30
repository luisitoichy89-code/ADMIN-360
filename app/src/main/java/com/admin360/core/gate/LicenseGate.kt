object LicenseGate {

    suspend fun isLicenseValid(negocioId: String): Boolean {

        val license = SupabaseClient
            .from("licencias")
            .select {
                filter {
                    eq("negocio_id", negocioId)
                }
            }
            .decodeSingleOrNull<LicenseDto>()

        return license?.estado == "ACTIVA"
    }
}
