package com.example.myapplication.ui.seller

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.SampleData
import com.example.myapplication.ui.theme.GoldAmber
import com.example.myapplication.ui.theme.SlateDark

@Composable
fun SellerProfile() {
    val context = LocalContext.current
    val usuario = SampleData.usuarioActual.value

    var nombreTienda by remember { mutableStateOf(usuario.nombre) }
    var emailContacto by remember { mutableStateOf(usuario.email) }
    var descripcionTienda by remember { mutableStateOf("Tienda oficial universitaria de vestuario, calzado y accesorios.") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(SlateDark),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = null,
                        tint = GoldAmber,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = usuario.nombre, style = MaterialTheme.typography.headlineMedium)
                Text(text = "Vendedor Verificado", style = MaterialTheme.typography.bodyMedium, color = GoldAmber)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = GoldAmber)
                            Text(text = "4.9", style = MaterialTheme.typography.titleLarge)
                        }
                        Text(text = "Calificación", style = MaterialTheme.typography.bodySmall)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "48", style = MaterialTheme.typography.titleLarge)
                        Text(text = "Ventas Totales", style = MaterialTheme.typography.bodySmall)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "6", style = MaterialTheme.typography.titleLarge)
                        Text(text = "Productos", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Información de la Tienda", style = MaterialTheme.typography.titleLarge)

                OutlinedTextField(
                    value = nombreTienda,
                    onValueChange = { nombreTienda = it },
                    label = { Text("Nombre Comercial") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = emailContacto,
                    onValueChange = { emailContacto = it },
                    label = { Text("Correo de Atención al Cliente") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcionTienda,
                    onValueChange = { descripcionTienda = it },
                    label = { Text("Descripción de la Tienda") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        SampleData.usuarioActual.value = usuario.copy(
                            nombre = nombreTienda,
                            email = emailContacto
                        )
                        Toast.makeText(context, "Información de la tienda actualizada", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "GUARDAR CAMBIOS", color = Color.White)
                }
            }
        }
    }
}
