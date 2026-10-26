package com.example.myapplication.ui.admin

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.myapplication.ui.theme.GoldAmber
import com.example.myapplication.ui.theme.SlateDark
import com.example.myapplication.ui.theme.StatusError

@Composable
fun AdminProducts() {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var showAddModal by remember { mutableStateOf(false) }
    var productToEdit by remember { mutableStateOf<Producto?>(null) }

    val filteredProducts = SampleData.productos.filter {
        it.nombre.contains(searchQuery, ignoreCase = true) || it.categoria.contains(searchQuery, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Catálogo General de Productos",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Control e inventario de todos los productos en la tienda",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar producto o categoría...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredProducts) { producto ->
                    AdminProductCard(
                        producto = producto,
                        onEdit = { productToEdit = producto },
                        onDelete = { prod ->
                            SampleData.productos.removeIf { it.id == prod.id }
                            Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show()
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
            Icon(imageVector = Icons.Default.Add, contentDescription = "Nuevo Producto")
        }
    }

    if (showAddModal) {
        ProductFormModal(
            title = "Agregar Producto",
            producto = null,
            onDismiss = { showAddModal = false },
            onSave = { newProd ->
                SampleData.productos.add(newProd)
                showAddModal = false
                Toast.makeText(context, "Producto añadido al catálogo", Toast.LENGTH_SHORT).show()
            }
        )
    }

    productToEdit?.let { prod ->
        ProductFormModal(
            title = "Editar Producto",
            producto = prod,
            onDismiss = { productToEdit = null },
            onSave = { updatedProd ->
                val index = SampleData.productos.indexOfFirst { it.id == updatedProd.id }
                if (index != -1) {
                    SampleData.productos[index] = updatedProd
                }
                productToEdit = null
                Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun AdminProductCard(
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
                    Text(text = "Categoría: ${producto.categoria}", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "$${String.format("%,.0f", producto.precio)} | Stock: ${producto.stock}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateDark
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

@Composable
fun ProductFormModal(
    title: String,
    producto: Producto?,
    onDismiss: () -> Unit,
    onSave: (Producto) -> Unit
) {
    var nombre by remember { mutableStateOf(producto?.nombre ?: "") }
    var categoria by remember { mutableStateOf(producto?.categoria ?: "") }
    var precioText by remember { mutableStateOf(producto?.precio?.toString() ?: "") }
    var stockText by remember { mutableStateOf(producto?.stock?.toString() ?: "") }
    var descripcion by remember { mutableStateOf(producto?.descripcion ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Producto") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = precioText,
                        onValueChange = { precioText = it },
                        label = { Text("Precio") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = stockText,
                        onValueChange = { stockText = it },
                        label = { Text("Stock") },
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val precio = precioText.toDoubleOrNull() ?: 0.0
                    val stock = stockText.toIntOrNull() ?: 0
                    if (nombre.isNotEmpty()) {
                        val newProd = Producto(
                            id = producto?.id ?: "p_${System.currentTimeMillis()}",
                            nombre = nombre,
                            descripcion = descripcion,
                            precio = precio,
                            stock = stock,
                            categoria = categoria,
                            imagenUrl = producto?.imagenUrl ?: "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=500"
                        )
                        onSave(newProd)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SlateDark)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
