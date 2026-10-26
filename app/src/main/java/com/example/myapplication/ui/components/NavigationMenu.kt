package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Rol
import com.example.myapplication.model.SampleData
import com.example.myapplication.ui.theme.GoldAmber
import com.example.myapplication.ui.theme.SlateDark
import com.example.myapplication.ui.theme.TextSecondary

data class DrawerItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

object NavigationRoutes {
    // Admin
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val USER_MANAGEMENT = "user_management"
    const val ADMIN_PRODUCTS = "admin_products"

    // Seller
    const val SELLER_PRODUCTS = "seller_products"
    const val ORDER_HISTORY = "order_history"
    const val SELLER_PROFILE = "seller_profile"

    // Buyer
    const val HOME = "home"
    const val CATALOG = "catalog"
    const val PRODUCT_DETAIL = "product_detail/{productId}"
    const val CART = "cart"
    const val CHECKOUT = "checkout"
    const val USER_PROFILE = "user_profile"

    // Auth
    const val LOGIN = "login"
    const val PASSWORD_RECOVERY = "password_recovery"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titulo: String,
    onOpenDrawer: () -> Unit,
    onNavigateToCart: () -> Unit,
    onLogout: () -> Unit
) {
    val cantidadCarrito = SampleData.carrito.sumOf { it.cantidad }
    val usuario = SampleData.usuarioActual.value

    TopAppBar(
        title = {
            Column {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Text(
                    text = "Rol: ${usuario.rol.label}",
                    style = MaterialTheme.typography.bodySmall,
                    color = GoldAmber
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onOpenDrawer) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = Color.White
                )
            }
        },
        actions = {
            if (usuario.rol == Rol.COMPRADOR) {
                IconButton(onClick = onNavigateToCart) {
                    BadgedBox(
                        badge = {
                            if (cantidadCarrito > 0) {
                                Badge(containerColor = GoldAmber, contentColor = Color.Black) {
                                    Text(text = cantidadCarrito.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito",
                            tint = Color.White
                        )
                    }
                }
            }
            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Cerrar Sesión",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SlateDark
        )
    )
}

@Composable
fun AppDrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val usuario = SampleData.usuarioActual.value
    var expandedRoleMenu by remember { mutableStateOf(false) }

    val items = when (usuario.rol) {
        Rol.ADMINISTRADOR -> listOf(
            DrawerItem(NavigationRoutes.ADMIN_DASHBOARD, "Panel de Control", Icons.Default.BarChart),
            DrawerItem(NavigationRoutes.USER_MANAGEMENT, "Gestión de Usuarios", Icons.Default.People),
            DrawerItem(NavigationRoutes.ADMIN_PRODUCTS, "Catálogo General", Icons.Default.ShoppingBag)
        )
        Rol.VENDEDOR -> listOf(
            DrawerItem(NavigationRoutes.SELLER_PRODUCTS, "Mis Productos", Icons.Default.Store),
            DrawerItem(NavigationRoutes.ORDER_HISTORY, "Pedidos Recibidos", Icons.Default.ReceiptLong),
            DrawerItem(NavigationRoutes.SELLER_PROFILE, "Perfil de Tienda", Icons.Default.Person)
        )
        Rol.COMPRADOR -> listOf(
            DrawerItem(NavigationRoutes.HOME, "Inicio", Icons.Default.Home),
            DrawerItem(NavigationRoutes.CATALOG, "Catálogo", Icons.Default.ShoppingBag),
            DrawerItem(NavigationRoutes.CART, "Mi Carrito", Icons.Default.ShoppingCart),
            DrawerItem(NavigationRoutes.USER_PROFILE, "Mi Perfil", Icons.Default.Person)
        )
    }

    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        // Cabecera del Usuario
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SlateDark)
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(GoldAmber),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = SlateDark,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = usuario.nombre,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Text(
                            text = usuario.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Selector rápido de Rol para demo universitaria
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedRoleMenu = true },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = null,
                                tint = GoldAmber
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cambiar Rol (${usuario.rol.label})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expandedRoleMenu,
                        onDismissRequest = { expandedRoleMenu = false }
                    ) {
                        Rol.entries.forEach { rol ->
                            DropdownMenuItem(
                                text = { Text(text = "Modo ${rol.label}") },
                                onClick = {
                                    SampleData.usuarioActual.value = usuario.copy(rol = rol)
                                    expandedRoleMenu = false
                                    when (rol) {
                                        Rol.ADMINISTRADOR -> onNavigate(NavigationRoutes.ADMIN_DASHBOARD)
                                        Rol.VENDEDOR -> onNavigate(NavigationRoutes.SELLER_PRODUCTS)
                                        Rol.COMPRADOR -> onNavigate(NavigationRoutes.HOME)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Opciones del Menú por Rol
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationDrawerItem(
                label = { Text(text = item.title) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = GoldAmber.copy(alpha = 0.2f),
                    selectedIconColor = SlateDark,
                    selectedTextColor = SlateDark
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Divider()

        // Botón Cerrar Sesión
        NavigationDrawerItem(
            label = { Text(text = "Cerrar Sesión") },
            icon = { Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null) },
            selected = false,
            onClick = onLogout,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val usuario = SampleData.usuarioActual.value

    val items = when (usuario.rol) {
        Rol.ADMINISTRADOR -> listOf(
            DrawerItem(NavigationRoutes.ADMIN_DASHBOARD, "Dashboard", Icons.Default.BarChart),
            DrawerItem(NavigationRoutes.USER_MANAGEMENT, "Usuarios", Icons.Default.People),
            DrawerItem(NavigationRoutes.ADMIN_PRODUCTS, "Productos", Icons.Default.ShoppingBag)
        )
        Rol.VENDEDOR -> listOf(
            DrawerItem(NavigationRoutes.SELLER_PRODUCTS, "Productos", Icons.Default.Store),
            DrawerItem(NavigationRoutes.ORDER_HISTORY, "Pedidos", Icons.Default.ReceiptLong),
            DrawerItem(NavigationRoutes.SELLER_PROFILE, "Perfil", Icons.Default.Person)
        )
        Rol.COMPRADOR -> listOf(
            DrawerItem(NavigationRoutes.HOME, "Inicio", Icons.Default.Home),
            DrawerItem(NavigationRoutes.CATALOG, "Catálogo", Icons.Default.ShoppingBag),
            DrawerItem(NavigationRoutes.CART, "Carrito", Icons.Default.ShoppingCart),
            DrawerItem(NavigationRoutes.USER_PROFILE, "Perfil", Icons.Default.Person)
        )
    }

    NavigationBar(
        containerColor = SlateDark,
        contentColor = Color.White
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (selected) GoldAmber else Color.White.copy(alpha = 0.6f)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (selected) GoldAmber else Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )
        }
    }
}
