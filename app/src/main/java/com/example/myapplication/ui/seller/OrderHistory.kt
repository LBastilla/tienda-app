package com.example.myapplication.ui.seller

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.EstadoPedido
import com.example.myapplication.model.Pedido
import com.example.myapplication.model.SampleData
import com.example.myapplication.ui.theme.GoldAmber
import com.example.myapplication.ui.theme.SlateDark
import com.example.myapplication.ui.theme.StatusSuccess

@Composable
fun OrderHistory() {
    val context = LocalContext.current
    val pedidos = SampleData.pedidos

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Historial de Pedidos Recibidos",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Gestiona los estados de envío de las compras recibidas",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pedidos) { pedido ->
                OrderItemCard(
                    pedido = pedido,
                    onStatusChange = { nuevoEstado ->
                        val index = SampleData.pedidos.indexOfFirst { it.id == pedido.id }
                        if (index != -1) {
                            SampleData.pedidos[index] = pedido.copy(estado = nuevoEstado)
                        }
                        Toast.makeText(context, "Estado del pedido actualizado a ${nuevoEstado.label}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
fun OrderItemCard(
    pedido: Pedido,
    onStatusChange: (EstadoPedido) -> Unit
) {
    var expandedStatusMenu by remember { mutableStateOf(false) }

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
                Column {
                    Text(text = pedido.id, style = MaterialTheme.typography.titleLarge)
                    Text(text = "Comprador: ${pedido.compradorNombre}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Fecha: ${pedido.fecha}", style = MaterialTheme.typography.bodySmall)
                }

                Button(
                    onClick = { expandedStatusMenu = true },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAmber)
                ) {
                    Text(text = pedido.estado.label, color = SlateDark)
                }

                DropdownMenu(
                    expanded = expandedStatusMenu,
                    onDismissRequest = { expandedStatusMenu = false }
                ) {
                    EstadoPedido.entries.forEach { estado ->
                        DropdownMenuItem(
                            text = { Text(estado.label) },
                            onClick = {
                                onStatusChange(estado)
                                expandedStatusMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Productos en este pedido:", style = MaterialTheme.typography.labelLarge)
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
                Text(text = "Método de Pago: ${pedido.metodoPago}", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "Total: $${String.format("%,.0f", pedido.total)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = StatusSuccess
                )
            }
        }
    }
}
