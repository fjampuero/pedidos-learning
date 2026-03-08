package com.codigo.pedido.orders.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class PedidoItem {

    private final UUID id;
    private final UUID pedidoId;
    private final UUID productoId;
    private final String nombreProducto;
    private final BigDecimal precio;
    private final int cantidad;
    private final BigDecimal subtotal;

    public PedidoItem(UUID id,
                      UUID pedidoId,
                      UUID productoId,
                      String nombreProducto,
                      BigDecimal precio,
                      int cantidad) {

        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio del item debe ser mayor a cero");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad del item debe ser mayor a cero");
        }

        this.id = id;
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
    }

    public UUID getId() {
        return id;
    }

    public UUID getPedidoId() {
        return pedidoId;
    }

    public UUID getProductoId() {
        return productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
