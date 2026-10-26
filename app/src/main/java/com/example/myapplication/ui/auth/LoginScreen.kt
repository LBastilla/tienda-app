package com.example.myapplication.ui.auth

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Rol
import com.example.myapplication.model.SampleData
import com.example.myapplication.ui.theme.GoldAmber
import com.example.myapplication.ui.theme.SlateDark

@Composable
fun LoginScreen(
    onLoginSuccess: (Rol) -> Unit,
    onNavigateToRecovery: () -> Unit
) {
    val context = LocalContext.current
    var usuarioText by remember { mutableStateOf("carlos.ruiz@gmail.com") }
    var contrasenaText by remember { mutableStateOf("123456") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateDark)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(GoldAmber),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = SlateDark,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Iniciar Sesión",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Ingresa tus credenciales para continuar",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Campo Usuario
                OutlinedTextField(
                    value = usuarioText,
                    onValueChange = { usuarioText = it },
                    label = { Text("Correo o Usuario") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Contraseña
                OutlinedTextField(
                    value = contrasenaText,
                    onValueChange = { contrasenaText = it },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Enlace Recuperación
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(onClick = onNavigateToRecovery) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Iniciar Sesión
                Button(
                    onClick = {
                        if (usuarioText.isNotEmpty() && contrasenaText.isNotEmpty()) {
                            val userFound = SampleData.usuarios.find { it.email.equals(usuarioText.trim(), ignoreCase = true) }
                            val rol = userFound?.rol ?: Rol.COMPRADOR
                            SampleData.usuarioActual.value = userFound ?: SampleData.usuarioActual.value.copy(
                                nombre = usuarioText.split("@")[0],
                                email = usuarioText,
                                rol = rol
                            )
                            Toast.makeText(context, "¡Bienvenido, ${SampleData.usuarioActual.value.nombre}!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess(rol)
                        } else {
                            Toast.makeText(context, "Por favor diligencie todos los campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "INGRESAR", color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón Autenticación Biométrica
                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Simulación: Huella dactilar verificada con éxito", Toast.LENGTH_SHORT).show()
                        val user = SampleData.usuarioActual.value
                        onLoginSuccess(user.rol)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = null,
                        tint = GoldAmber
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Ingresar con Huella / Biometría", color = SlateDark)
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Acceso Rápido por Rol (Demostración):",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            val user = SampleData.usuarios.first { it.rol == Rol.ADMINISTRADOR }
                            SampleData.usuarioActual.value = user
                            onLoginSuccess(Rol.ADMINISTRADOR)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAmber),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Admin", color = Color.Black, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = {
                            val user = SampleData.usuarios.first { it.rol == Rol.VENDEDOR }
                            SampleData.usuarioActual.value = user
                            onLoginSuccess(Rol.VENDEDOR)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = SlateDark),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Vendedor", color = Color.White, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = {
                            val user = SampleData.usuarios.first { it.rol == Rol.COMPRADOR }
                            SampleData.usuarioActual.value = user
                            onLoginSuccess(Rol.COMPRADOR)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = SlateDark.copy(alpha = 0.8f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Comprador", color = Color.White, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
