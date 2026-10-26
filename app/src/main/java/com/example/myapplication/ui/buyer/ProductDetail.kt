package com.example.myapplication.ui.buyer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.model.ItemCarrito
import com.example.myapplication.model.SampleData
import com.example.myapplication.ui.theme.GoldAmber
import com.example.myapplication.ui.theme.SlateDark

@Composable
fun ProductDetail(
    productId: String,
    onBack: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val context = LocalContext.current
    val producto = SampleData.productos.find { it.id == productId } ?: SampleData.productos.first()
    var cantidad by remember { mutableIntStateOf(1) }

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
                text = "Detalle del Producto",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            AsyncImage(
                model = producto.imagenUrl,
                contentDescription = producto.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = producto.nombre, style = MaterialTheme.typography.headlineMedium)
        Text(text = "Categoría: ${producto.categoria}", style = MaterialTheme.typography.bodyMedium, color = GoldAmber)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "$${String.format("%,.0f", producto.precio)}",
            style = MaterialTheme.typography.headlineLarge,
            color = SlateDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = producto.descripcion,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Stock disponible: ${producto.stock} unidades",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (cantidad > 1) cantidad-- },
                modifier = Modifier.background(GoldAmber, RoundedCornerShape(8.dp))
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Menos", tint = SlateDark)
            }

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = cantidad.toString(),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.width(20.dp))

            IconButton(
                onClick = { if (cantidad < producto.stock) cantidad++ },
                modifier = Modifier.background(GoldAmber, RoundedCornerShape(8.dp))
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Más", tint = SlateDark)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val existingItem = SampleData.carrito.find { it.producto.id == producto.id }
                if (existingItem != null) {
                    existingItem.cantidad += cantidad
                } else {
                    SampleData.carrito.add(ItemCarrito(producto, cantidad))
                }
                Toast.makeText(context, "¡Producto agregado al carrito!", Toast.LENGTH_SHORT).show()
                onNavigateToCart()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SlateDark),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "AGREGAR AL CARRITO", color = Color.White)
        }
    }
}
