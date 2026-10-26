package com.example.myapplication.model

enum class Rol(val label: String) {
    ADMINISTRADOR("Administrador"),
    VENDEDOR("Vendedor"),
    COMPRADOR("Comprador")
}

enum class EstadoPedido(val label: String) {
    PENDIENTE("Pendiente"),
    EN_PROCESO("En Proceso"),
    ENVIADO("Enviado"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado")
}

data class Usuario(
    val id: String,
    val nombre: String,
    val email: String,
    val rol: Rol,
    val activo: Boolean = true,
    val fechaRegistro: String = "2025-01-10"
)

data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val categoria: String,
    val imagenUrl: String,
    val vendedorId: String = "vend_1"
)

data class ItemCarrito(
    val producto: Producto,
    var cantidad: Int
) {
    val subtotal: Double
        get() = producto.precio * cantidad
}

data class Pedido(
    val id: String,
    val compradorId: String,
    val compradorNombre: String,
    val items: List<ItemCarrito>,
    val total: Double,
    var estado: EstadoPedido,
    val fecha: String,
    val metodoPago: String = "Tarjeta de Crédito"
)

data class MetricaDashboard(
    val totalVentas: Double,
    val usuariosActivos: Int,
    val pedidosTotales: Int,
    val productosCatalogo: Int
)
