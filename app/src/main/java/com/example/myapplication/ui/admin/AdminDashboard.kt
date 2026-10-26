package com.example.myapplication.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.SampleData
import com.example.myapplication.ui.theme.GoldAmber
import com.example.myapplication.ui.theme.SlateDark
import com.example.myapplication.ui.theme.StatusSuccess

@Composable
fun AdminDashboard(
    onNavigateToUsers: () -> Unit,
    onNavigateToProducts: () -> Unit
) {
    val metricas = SampleData.obtenerMetricasAdmin()
    val pedidosRecientes = SampleData.pedidos.take(5)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Panel de Control Administrativo",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Resumen general de métricas y rendimiento del negocio",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Fila 1 de Métricas KPI
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KpiCard(
                    title = "Total Ventas",
                    value = "$${String.format("%,.0f", metricas.totalVentas)}",
                    icon = Icons.Default.AttachMoney,
                    color = StatusSuccess,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = "Usuarios Activos",
                    value = "${metricas.usuariosActivos}",
                    icon = Icons.Default.People,
                    color = SlateDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Fila 2 de Métricas KPI
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KpiCard(
                    title = "Pedidos Totales",
                    value = "${metricas.pedidosTotales}",
                    icon = Icons.Default.ReceiptLong,
                    color = GoldAmber,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = "Productos en Catálogo",
                    value = "${metricas.productosCatalogo}",
                    icon = Icons.Default.ShoppingBag,
                    color = SlateDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Accesos Directos
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Accesos Directos de Administración",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onNavigateToUsers,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SlateDark),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.People, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Gestionar Usuarios")
                        }
                        Button(
                            onClick = onNavigateToProducts,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAmber),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ver Productos", color = Color.Black)
                        }
                    }
                }
            }
        }

        // Tabla/Lista de Pedidos Recientes
        item {
            Text(
                text = "Pedidos Recientes",
                style = MaterialTheme.typography.titleLarge
            )
        }

        items(pedidosRecientes) { pedido ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = pedido.id,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Cliente: ${pedido.compradorNombre}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Fecha: ${pedido.fecha}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$${String.format("%,.0f", pedido.total)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = SlateDark
                        )
                        Text(
                            text = pedido.estado.label,
                            style = MaterialTheme.typography.bodySmall,
                            color = StatusSuccess
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(color.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = color)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
