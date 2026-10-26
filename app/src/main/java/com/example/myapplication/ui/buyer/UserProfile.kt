package com.example.myapplication.ui.buyer

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.SampleData
import com.example.myapplication.ui.theme.GoldAmber
import com.example.myapplication.ui.theme.SlateDark
import com.example.myapplication.ui.theme.StatusSuccess

@Composable
fun UserProfile() {
    val usuario = SampleData.usuarioActual.value
    val misPedidos = SampleData.pedidos.filter { it.compradorId == usuario.id || it.compradorId == "usr_1" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cabecera Perfil
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateDark),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(GoldAmber),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = SlateDark,
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = usuario.nombre,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
                        Text(
                            text = usuario.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = GoldAmber
                        )
                        Text(
                            text = "Miembro desde: ${usuario.fechaRegistro}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        // Título Historial de Compras
        item {
            Text(
                text = "Mi Historial de Compras",
                style = MaterialTheme.typography.titleLarge
            )
        }

        items(misPedidos) { pedido ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = null, tint = SlateDark)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = pedido.id, style = MaterialTheme.typography.titleLarge)
                        }
                        Text(
                            text = pedido.estado.label,
                            style = MaterialTheme.typography.titleMedium,
                            color = StatusSuccess
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Fecha: ${pedido.fecha}", style = MaterialTheme.typography.bodySmall)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    pedido.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${item.cantidad}x ${item.producto.nombre}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "$${String.format("%,.0f", item.subtotal)}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Pago: ${pedido.metodoPago}", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = "Total: $${String.format("%,.0f", pedido.total)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = SlateDark
                        )
                    }
                }
            }
        }
    }
}
