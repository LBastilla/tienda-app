package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaHome(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class Producto(
    val id: String,
    val nombre: String,
    val precio: Double,
    val descripcion: String = "",
    val imagen: String = ""
)

@Composable
fun PantallaHome(modifier: Modifier = Modifier) {
    val productos = listOf(
        Producto(
            id = "1",
            nombre = "Camiseta",
            precio = 29.99,
            descripcion = "Camiseta de algodón 100% suave y transpirable.",
            imagen = "https://arturocalle.vtexassets.com/arquivos/ids/903311-1200-1600?v=639174113789970000&width=1200&height=1600&aspect=true"
        ),
        Producto(
            id = "2",
            nombre = "Zapatos",
            precio = 59.99,
            descripcion = "Zapatos de cuero elegantes y cómodos para uso diario.",
            imagen = "https://images.unsplash.com/photo-1549298916-b41d501d3772"
        ),
        Producto(
            id = "3",
            nombre = "Bolso",
            precio = 39.99,
            descripcion = "Bolso de mano espacioso y resistente.",
            imagen = "https://images.unsplash.com/photo-1584917865442-de89df76afd3"
        ),
        Producto(
            id = "4",
            nombre = "Chaqueta",
            precio = 89.99,
            descripcion = "Chaqueta impermeable ideal para clima frío.",
            imagen = "https://images.unsplash.com/photo-1551028719-00167b16eac5"
        ),
        Producto(
            id = "5",
            nombre = "Gafas de Sol",
            precio = 19.99,
            descripcion = "Gafas de sol con protección UV400 y diseño moderno.",
            imagen = "https://images.unsplash.com/photo-1511499767150-a48a237f0083"
        ),
        Producto(
            id = "6",
            nombre = "Reloj Deportivo",
            precio = 119.99,
            descripcion = "Reloj resistente al agua con monitor de ritmo cardíaco.",
            imagen = "https://images.unsplash.com/photo-1523275335684-37898b6baf30"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Catálogo de Productos",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(productos) { producto ->
            ItemProducto(producto = producto)
        }
    }
}

@Composable
fun ItemProducto(producto: Producto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "$${producto.precio}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )
            if (producto.imagen.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "URL Imagen: ${producto.imagen}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
