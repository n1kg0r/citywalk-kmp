package org.nikgor.project.screens

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import kotlinx.coroutines.launch
import org.nikgor.project.data.RoutePlan
import org.nikgor.project.routing.RoutePlanner

@Composable
fun HomeScreen() {
    var city by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("3") }
    var loading by remember { mutableStateOf(false) }
    var plan by remember { mutableStateOf<RoutePlan?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("MarcheRoute", style = MaterialTheme.typography.headlineMedium)

        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") }
        )

        TextField(
            value = hours,
            onValueChange = { hours = it },
            label = { Text("Hours available") }
        )

        Button(
            enabled = !loading,
            onClick = {
                scope.launch {
                    loading = true
                    plan = RoutePlanner().planRoute(
                        city = city,
                        hours = hours.toDoubleOrNull() ?: 3.0
                    )
                    loading = false
                }
            }
        ) {
            Text("Generate route")
        }

        if (loading) {
            CircularProgressIndicator()
        }

        plan?.let {
            Text("Stops:")
            it.stops.forEachIndexed { i, poi ->
                Text("${i + 1}. ${poi.name}")
            }
        }
    }
}
