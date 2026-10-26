package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.model.Rol
import com.example.myapplication.model.SampleData
import com.example.myapplication.ui.admin.AdminDashboard
import com.example.myapplication.ui.admin.AdminProducts
import com.example.myapplication.ui.admin.UserManagement
import com.example.myapplication.ui.auth.LoginScreen
import com.example.myapplication.ui.auth.PasswordRecoveryScreen
import com.example.myapplication.ui.buyer.CartScreen
import com.example.myapplication.ui.buyer.CatalogScreen
import com.example.myapplication.ui.buyer.CheckoutScreen
import com.example.myapplication.ui.buyer.HomeScreen
import com.example.myapplication.ui.buyer.ProductDetail
import com.example.myapplication.ui.buyer.UserProfile
import com.example.myapplication.ui.components.AppDrawerContent
import com.example.myapplication.ui.components.AppTopBar
import com.example.myapplication.ui.components.BottomNavBar
import com.example.myapplication.ui.components.NavigationRoutes
import com.example.myapplication.ui.seller.OrderHistory
import com.example.myapplication.ui.seller.SellerProducts
import com.example.myapplication.ui.seller.SellerProfile
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppStructure()
            }
        }
    }
}

@Composable
fun MainAppStructure() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: NavigationRoutes.LOGIN

    val isAuthScreen = currentRoute == NavigationRoutes.LOGIN || currentRoute == NavigationRoutes.PASSWORD_RECOVERY

    val title = when (currentRoute) {
        NavigationRoutes.ADMIN_DASHBOARD -> "Panel de Control"
        NavigationRoutes.USER_MANAGEMENT -> "Gestión de Usuarios"
        NavigationRoutes.ADMIN_PRODUCTS -> "Catálogo General"
        NavigationRoutes.SELLER_PRODUCTS -> "Mis Productos"
        NavigationRoutes.ORDER_HISTORY -> "Pedidos Recibidos"
        NavigationRoutes.SELLER_PROFILE -> "Perfil de Tienda"
        NavigationRoutes.HOME -> "Tienda Virtual"
        NavigationRoutes.CATALOG -> "Catálogo de Productos"
        NavigationRoutes.CART -> "Carrito de Compras"
        NavigationRoutes.CHECKOUT -> "Pasarela de Pagos"
        NavigationRoutes.USER_PROFILE -> "Mi Perfil"
        else -> "Tienda Virtual"
    }






    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !isAuthScreen,
        drawerContent = {
            if (!isAuthScreen) {
                AppDrawerContent(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    },
                    onLogout = {
                        coroutineScope.launch { drawerState.close() }
                        SampleData.carrito.clear()
                        navController.navigate(NavigationRoutes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (!isAuthScreen) {
                    AppTopBar(
                        titulo = title,
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        },
                        onNavigateToCart = {
                            navController.navigate(NavigationRoutes.CART)
                        },
                        onLogout = {
                            SampleData.carrito.clear()
                            navController.navigate(NavigationRoutes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            },
            bottomBar = {
                if (!isAuthScreen) {
                    BottomNavBar(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = NavigationRoutes.LOGIN,
                modifier = Modifier.padding(innerPadding)
            ) {
                // Auth
                composable(NavigationRoutes.LOGIN) {
                    LoginScreen(
                        onLoginSuccess = { rol ->
                            val startRoute = when (rol) {
                                Rol.ADMINISTRADOR -> NavigationRoutes.ADMIN_DASHBOARD
                                Rol.VENDEDOR -> NavigationRoutes.SELLER_PRODUCTS
                                Rol.COMPRADOR -> NavigationRoutes.HOME
                            }
                            navController.navigate(startRoute) {
                                popUpTo(NavigationRoutes.LOGIN) { inclusive = true }
                            }
                        },
                        onNavigateToRecovery = {
                            navController.navigate(NavigationRoutes.PASSWORD_RECOVERY)
                        }
                    )
                }
                composable(NavigationRoutes.PASSWORD_RECOVERY) {
                    PasswordRecoveryScreen(
                        onBackToLogin = { navController.popBackStack() }
                    )
                }

                // Admin
                composable(NavigationRoutes.ADMIN_DASHBOARD) {
                    AdminDashboard(
                        onNavigateToUsers = { navController.navigate(NavigationRoutes.USER_MANAGEMENT) },
                        onNavigateToProducts = { navController.navigate(NavigationRoutes.ADMIN_PRODUCTS) }
                    )
                }
                composable(NavigationRoutes.USER_MANAGEMENT) {
                    UserManagement()
                }
                composable(NavigationRoutes.ADMIN_PRODUCTS) {
                    AdminProducts()
                }

                // Seller
                composable(NavigationRoutes.SELLER_PRODUCTS) {
                    SellerProducts()
                }
                composable(NavigationRoutes.ORDER_HISTORY) {
                    OrderHistory()
                }
                composable(NavigationRoutes.SELLER_PROFILE) {
                    SellerProfile()
                }

                // Buyer
                composable(NavigationRoutes.HOME) {
                    HomeScreen(
                        onNavigateToCatalog = { navController.navigate(NavigationRoutes.CATALOG) },
                        onNavigateToProductDetail = { productId ->
                            navController.navigate("product_detail/$productId")
                        }
                    )
                }
                composable(NavigationRoutes.CATALOG) {
                    CatalogScreen(
                        onNavigateToDetail = { productId ->
                            navController.navigate("product_detail/$productId")
                        }
                    )
                }
                composable(
                    route = NavigationRoutes.PRODUCT_DETAIL,
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    ProductDetail(
                        productId = productId,
                        onBack = { navController.popBackStack() },
                        onNavigateToCart = { navController.navigate(NavigationRoutes.CART) }
                    )
                }
                composable(NavigationRoutes.CART) {
                    CartScreen(
                        onNavigateToCheckout = { navController.navigate(NavigationRoutes.CHECKOUT) },
                        onNavigateToCatalog = { navController.navigate(NavigationRoutes.CATALOG) }
                    )
                }
                composable(NavigationRoutes.CHECKOUT) {
                    CheckoutScreen(
                        onBack = { navController.popBackStack() },
                        onOrderFinished = {
                            navController.navigate(NavigationRoutes.HOME) {
                                popUpTo(NavigationRoutes.HOME) { inclusive = true }
                            }
                        }
                    )
                }
                composable(NavigationRoutes.USER_PROFILE) {
                    UserProfile()
                }
            }
        }
    }
}
