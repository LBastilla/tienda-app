package com.example.myapplication.ui.buyer

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.model.ItemCarrito
import com.example.myapplication.model.SampleData
import com.example.myapplication.ui.theme.GoldAmber
import com.example.myapplication.ui.theme.SlateDark

@Composable
fun CartScreen(
    onNavigateToCheckout: () -> Unit,
    onNavigateToCatalog: () -> Unit
) {
    val cartItems = SampleData.carrito
    val subtotal = cartItems.sumOf { it.subtotal }
    val envio = if (subtotal > 0) 8000.0 else 0.0
    val total = subtotal + envio

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Carrito de Compras",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Resumen de productos seleccionados para compra",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = GoldAmber,
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tu carrito está vacío",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onNavigateToCatalog,
                        colors = ButtonDefaults.buttonColors(containerColor = SlateDark)
                    ) {
                        Text("Ir al Catálogo")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems) { item ->
                    CartItemCard(
                        item = item,
                        onQuantityChange = { newQty ->
                            val index = SampleData.carrito.indexOfFirst { it.producto.id == item.producto.id }
                            if (index != -1) {
                                if (newQty > 0) {
                                    SampleData.carrito[index] = item.copy(cantidad = newQty)
                                } else {
                                    SampleData.carrito.removeAt(index)
                                }
                            }
                        },
                        onRemove = {
                            SampleData.carrito.removeIf { it.producto.id == item.producto.id }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Subtotal", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "$${String.format("%,.0f", subtotal)}", style = MaterialTheme.typography.bodyLarge)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Envío", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "$${String.format("%,.0f", envio)}", style = MaterialTheme.typography.bodyLarge)
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "TOTAL", style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = "$${String.format("%,.0f", total)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = SlateDark
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onNavigateToCheckout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAmber),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "PROCEDER AL PAGO", color = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: ItemCarrito,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.producto.imagenUrl,
                contentDescription = item.producto.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.producto.nombre, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "$${String.format("%,.0f", item.subtotal)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = SlateDark
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onQuantityChange(item.cantidad - 1) }) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Restar")
                }
                Text(text = item.cantidad.toString(), style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = { onQuantityChange(item.cantidad + 1) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Sumar")
                }
                IconButton(onClick = onRemove) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}
