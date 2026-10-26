package com.example.myapplication.ui.buyer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.EstadoPedido
import com.example.myapplication.model.Pedido
import com.example.myapplication.model.SampleData
import com.example.myapplication.ui.theme.GoldAmber
import com.example.myapplication.ui.theme.SlateDark
import com.example.myapplication.ui.theme.StatusSuccess

@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    onOrderFinished: () -> Unit
) {
    val cartItems = SampleData.carrito.toList()
    val subtotal = cartItems.sumOf { it.subtotal }
    val total = subtotal + 8000.0

    var direccion by remember { mutableStateOf("Edificio Principal, Aula 302, Campus Central") }
    var telefono by remember { mutableStateOf("+57 310 987 6543") }
    var metodoPagoSeleccionado by remember { mutableStateOf("Tarjeta de Crédito") }
    var showSuccessModal by remember { mutableStateOf(false) }

    val metodosPago = listOf("Tarjeta de Crédito", "Efectivo contra entrega", "PSE / Transferencia")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Pasarela de Pagos",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dirección de Entrega
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null, tint = SlateDark)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Dirección de Envío (Campus)", style = MaterialTheme.typography.titleLarge)
                }

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Lugar de Entrega / Edificio") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono de Contacto") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Método de Pago
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Payments, contentDescription = null, tint = SlateDark)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Método de Pago", style = MaterialTheme.typography.titleLarge)
                }

                metodosPago.forEach { metodo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { metodoPagoSeleccionado = metodo },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (metodo == metodoPagoSeleccionado),
                            onClick = { metodoPagoSeleccionado = metodo }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, tint = SlateDark)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = metodo, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Resumen Final y Botón
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total a Pagar", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    Text(
                        text = "$${String.format("%,.0f", total)}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = GoldAmber
                    )
                }

                Button(
                    onClick = {
                        val usuario = SampleData.usuarioActual.value
                        val nuevoPedido = Pedido(
                            id = "PED-${System.currentTimeMillis().toString().takeLast(4)}",
                            compradorId = usuario.id,
                            compradorNombre = usuario.nombre,
                            items = cartItems,
                            total = total,
                            estado = EstadoPedido.PENDIENTE,
                            fecha = "2025-02-22",
                            metodoPago = metodoPagoSeleccionado
                        )
                        SampleData.pedidos.add(0, nuevoPedido)
                        SampleData.carrito.clear()
                        showSuccessModal = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAmber),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "CONFIRMAR COMPRA", color = Color.Black)
                }
            }
        }
    }

    if (showSuccessModal) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(StatusSuccess.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(40.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "¡Compra Exitosa!", style = MaterialTheme.typography.headlineMedium)
                }
            },
            text = {
                Text(
                    text = "Tu pedido ha sido registrado con éxito. Puedes consultar el estado del envío desde tu Perfil.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessModal = false
                        onOrderFinished()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al Inicio")
                }
            }
        )
    }
}
