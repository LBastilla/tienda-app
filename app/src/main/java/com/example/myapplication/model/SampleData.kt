package com.example.myapplication.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

object SampleData {

    // Usuario activo actual en sesión
    val usuarioActual = mutableStateOf(
        Usuario(
            id = "usr_1",
            nombre = "Carlos Andrés Ruiz",
            email = "carlos.ruiz@gmail.com",
            rol = Rol.COMPRADOR
        )
    )

    // Lista de usuarios registrada
    val usuarios = mutableStateListOf(
        Usuario("usr_1", "Carlos Andrés Ruiz", "carlos.ruiz@gmail.com", Rol.COMPRADOR),
        Usuario("usr_2", "María Fernanda Gómez", "m.gomez@gmail.com", Rol.VENDEDOR),
        Usuario("usr_3", "Admin General", "admin@gmail.com", Rol.ADMINISTRADOR),
        Usuario("usr_4", "Juan Pablo Silva", "j.silva@gmail.com", Rol.COMPRADOR),
        Usuario("usr_5", "TecnoTienda S.A.S.", "contacto@gmail.com", Rol.VENDEDOR)
    )

    // Catálogo de Productos
    val productos = mutableStateListOf(
        Producto(
            id = "p1",
            nombre = "Camiseta Algodón Premium",
            descripcion = "Camiseta de algodón 100% transpirable con corte moderno.",
            precio = 45000.0,
            stock = 25,
            categoria = "Ropa",
            imagenUrl = "https://images.unsplash.com/photo-1583743814966-8936f5b7be1a?w=500",
            vendedorId = "usr_2"
        ),
        Producto(
            id = "2",
            nombre = "Zapatos Deportivos Urban",
            descripcion = "Calzado cómodo para uso diario con suela antideslizante.",
            precio = 189000.0,
            stock = 12,
            categoria = "Calzado",
            imagenUrl = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500",
            vendedorId = "usr_2"
        ),
        Producto(
            id = "3",
            nombre = "Mochila Ergonómica Pro",
            descripcion = "Mochila para portátil hasta 15.6 pulgadas con puerto USB de carga.",
            precio = 120000.0,
            stock = 18,
            categoria = "Accesorios",
            imagenUrl = "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500",
            vendedorId = "usr_5"
        ),
        Producto(
            id = "4",
            nombre = "Chaqueta Cortavientos",
            precio = 135000.0,
            descripcion = "Chaqueta liviana impermeable ideal para días fríos o lluviosos.",
            stock = 8,
            categoria = "Ropa",
            imagenUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=500",
            vendedorId = "usr_2"
        ),
        Producto(
            id = "5",
            nombre = "Gafas de Sol UV400",
            precio = 49900.0,
            descripcion = "Gafas de sol con filtro UV400 y marco de acetato resistente.",
            stock = 30,
            categoria = "Accesorios",
            imagenUrl = "https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=500",
            vendedorId = "usr_5"
        ),
        Producto(
            id = "6",
            nombre = "Audífonos Inalámbricos Bluetooth",
            precio = 155000.0,
            descripcion = "Audífonos TWS con cancelación de ruido y batería de hasta 24 horas.",
            stock = 15,
            categoria = "Tecnología",
            imagenUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500",
            vendedorId = "usr_5"
        )
    )

    // Carrito de compras actual
    val carrito = mutableStateListOf<ItemCarrito>(
        ItemCarrito(productos[0], 2),
        ItemCarrito(productos[2], 1)
    )

    // Historial de pedidos
    val pedidos = mutableStateListOf(
        Pedido(
            id = "PED-1001",
            compradorId = "usr_1",
            compradorNombre = "Carlos Andrés Ruiz",
            items = listOf(ItemCarrito(productos[0], 1), ItemCarrito(productos[1], 1)),
            total = 234000.0,
            estado = EstadoPedido.ENVIADO,
            fecha = "2025-02-18",
            metodoPago = "Tarjeta de Crédito"
        ),
        Pedido(
            id = "PED-1002",
            compradorId = "usr_4",
            compradorNombre = "Juan Pablo Silva",
            items = listOf(ItemCarrito(productos[2], 1)),
            total = 120000.0,
            estado = EstadoPedido.PENDIENTE,
            fecha = "2025-02-20",
            metodoPago = "Efectivo contra entrega"
        ),
        Pedido(
            id = "PED-1003",
            compradorId = "usr_1",
            compradorNombre = "Carlos Andrés Ruiz",
            items = listOf(ItemCarrito(productos[5], 1)),
            total = 155000.0,
            estado = EstadoPedido.ENTREGADO,
            fecha = "2025-02-10",
            metodoPago = "PSE / Transferencia"
        )
    )

    fun obtenerMetricasAdmin(): MetricaDashboard {
        val totalVentas = pedidos.sumOf { it.total }
        val usuariosActivos = usuarios.count { it.activo }
        return MetricaDashboard(
            totalVentas = totalVentas,
            usuariosActivos = usuariosActivos,
            pedidosTotales = pedidos.size,
            productosCatalogo = productos.size
        )
    }
}
