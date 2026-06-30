@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigate: (String) -> Unit
) {

    val state = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    if (state.loading) {
        CircularProgressIndicator()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Dashboard")

        Spacer(Modifier.height(20.dp))

        Row {
            KpiCard("Negocios", state.negocios.toString())
            KpiCard("Locales", state.locales.toString())
        }

        Spacer(Modifier.height(12.dp))

        Row {
            KpiCard("Usuarios", state.usuarios.toString())
            KpiCard("Licencias", state.licenciasActivas.toString())
        }

        Spacer(Modifier.height(20.dp))

        Button(onClick = { onNavigate("negocios") }) {
            Text("Gestionar negocios")
        }
    }
}
