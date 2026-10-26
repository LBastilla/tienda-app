package com.example.myapplication.ui.seller

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.model.Producto
import com.example.myapplication.model.SampleData
import com.example.myapplication.ui.admin.ProductFormModal
import com.example.myapplication.ui.theme.GoldAmber
import com.example.myapplication.ui.theme.SlateDark
import com.example.myapplication.ui.theme.StatusError

@Composable
fun SellerProducts() {
    val context = LocalContext.current
    val currentUserId = SampleData.usuarioActual.value.id
    val sellerProducts = SampleData.productos.filter { it.vendedorId == currentUserId || it.vendedorId == "usr_2" }

    var showAddModal by remember { mutableStateOf(false) }
    var productToEdit by remember { mutableStateOf<Producto?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Panel de Gestión de Productos",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Administra el stock y precio de tus productos publicados",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sellerProducts) { producto ->
                    SellerProductItem(
                        producto = producto,
                        onEdit = { productToEdit = producto },
                        onDelete = { prod ->
                            SampleData.productos.removeIf { it.id == prod.id }
                            Toast.makeText(context, "Producto retirado del catálogo", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddModal = true },
            containerColor = GoldAmber,
            contentColor = SlateDark,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Publicar Producto")
        }
    }

    if (showAddModal) {
        ProductFormModal(
            title = "Publicar Nuevo Producto",
            producto = null,
            onDismiss = { showAddModal = false },
            onSave = { newProd ->
                SampleData.productos.add(newProd.copy(vendedorId = currentUserId))
                showAddModal = false
                Toast.makeText(context, "¡Producto publicado con éxito!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    productToEdit?.let { prod ->
        ProductFormModal(
            title = "Editar Publicación",
            producto = prod,
            onDismiss = { productToEdit = null },
            onSave = { updatedProd ->
                val index = SampleData.productos.indexOfFirst { it.id == updatedProd.id }
                if (index != -1) {
                    SampleData.productos[index] = updatedProd
                }
                productToEdit = null
                Toast.makeText(context, "Publicación actualizada", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun SellerProductItem(
    producto: Producto,
    onEdit: () -> Unit,
    onDelete: (Producto) -> Unit
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
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model = producto.imagenUrl,
                    contentDescription = producto.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = producto.nombre, style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "$${String.format("%,.0f", producto.precio)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = SlateDark
                    )
                    Text(
                        text = "Stock disponible: ${producto.stock} unidades",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = SlateDark)
                }
                IconButton(onClick = { onDelete(producto) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = StatusError)
                }
            }
        }
    }
}
