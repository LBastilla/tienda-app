package com.example.myapplication.ui.admin

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Rol
import com.example.myapplication.model.SampleData
import com.example.myapplication.model.Usuario
import com.example.myapplication.ui.theme.GoldAmber
import com.example.myapplication.ui.theme.SlateDark
import com.example.myapplication.ui.theme.StatusError
import com.example.myapplication.ui.theme.StatusSuccess

@Composable
fun UserManagement() {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var showAddModal by remember { mutableStateOf(false) }
    var userToEdit by remember { mutableStateOf<Usuario?>(null) }

    val filteredUsers = SampleData.usuarios.filter {
        it.nombre.contains(searchQuery, ignoreCase = true) || it.email.contains(searchQuery, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Gestión de Usuarios",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Listado completo de usuarios con opciones de administración CRUD",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Buscador
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar usuario por nombre o correo...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredUsers) { user ->
                    UserCard(
                        user = user,
                        onEdit = { userToEdit = user },
                        onToggleActive = { updatedUser ->
                            val index = SampleData.usuarios.indexOfFirst { it.id == updatedUser.id }
                            if (index != -1) {
                                SampleData.usuarios[index] = updatedUser
                            }
                        },
                        onDelete = { userToDelete ->
                            SampleData.usuarios.removeIf { it.id == userToDelete.id }
                            Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        // Botón Flotante para Agregar Usuario
        FloatingActionButton(
            onClick = { showAddModal = true },
            containerColor = GoldAmber,
            contentColor = SlateDark,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Nuevo Usuario")
        }
    }

    // Modal Crear Usuario
    if (showAddModal) {
        UserFormModal(
            title = "Crear Nuevo Usuario",
            user = null,
            onDismiss = { showAddModal = false },
            onSave = { newUser ->
                SampleData.usuarios.add(newUser)
                showAddModal = false
                Toast.makeText(context, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Modal Editar Usuario
    userToEdit?.let { user ->
        UserFormModal(
            title = "Editar Usuario",
            user = user,
            onDismiss = { userToEdit = null },
            onSave = { updatedUser ->
                val index = SampleData.usuarios.indexOfFirst { it.id == updatedUser.id }
                if (index != -1) {
                    SampleData.usuarios[index] = updatedUser
                }
                userToEdit = null
                Toast.makeText(context, "Usuario actualizado", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun UserCard(
    user: Usuario,
    onEdit: () -> Unit,
    onToggleActive: (Usuario) -> Unit,
    onDelete: (Usuario) -> Unit
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
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(SlateDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = GoldAmber)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = user.nombre, style = MaterialTheme.typography.titleLarge)
                    Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Rol: ${user.rol.label}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateDark
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (user.activo) "Activo" else "Inactivo",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (user.activo) StatusSuccess else StatusError
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = user.activo,
                    onCheckedChange = { isActive ->
                        onToggleActive(user.copy(activo = isActive))
                    }
                )
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = SlateDark)
                }
                IconButton(onClick = { onDelete(user) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = StatusError)
                }
            }
        }
    }
}

@Composable
fun UserFormModal(
    title: String,
    user: Usuario?,
    onDismiss: () -> Unit,
    onSave: (Usuario) -> Unit
) {
    var nombre by remember { mutableStateOf(user?.nombre ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var selectedRol by remember { mutableStateOf(user?.rol ?: Rol.COMPRADOR) }
    var expandedRolMenu by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )
                Box {
                    OutlinedButton(
                        onClick = { expandedRolMenu = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Rol: ${selectedRol.label}")
                    }
                    DropdownMenu(
                        expanded = expandedRolMenu,
                        onDismissRequest = { expandedRolMenu = false }
                    ) {
                        Rol.entries.forEach { rol ->
                            DropdownMenuItem(
                                text = { Text(rol.label) },
                                onClick = {
                                    selectedRol = rol
                                    expandedRolMenu = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotEmpty() && email.isNotEmpty()) {
                        val newUser = Usuario(
                            id = user?.id ?: "usr_${System.currentTimeMillis()}",
                            nombre = nombre,
                            email = email,
                            rol = selectedRol,
                            activo = user?.activo ?: true
                        )
                        onSave(newUser)
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
