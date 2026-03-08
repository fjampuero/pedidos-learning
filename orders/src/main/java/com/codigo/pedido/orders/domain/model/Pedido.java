package com.codigo.pedido.orders.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Pedido {

    private final UUID id;
    private final UUID usuarioId;
    private final UUID restauranteId;
    private String estado;
    private final BigDecimal total;
    private final LocalDateTime fechaCreacion;
    private final List<PedidoItem> items;

    public Pedido(UUID id,
                  UUID usuarioId,
                  UUID restauranteId,
                  String estado,
                  LocalDateTime fechaCreacion,
                  List<PedidoItem> items) {

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un item");
        }

        this.id = id;
        this.usuarioId = usuarioId;
        this.restauranteId = restauranteId;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion == null ? LocalDateTime.now() : fechaCreacion;
        this.items = List.copyOf(items);
        this.total = items.stream()
                .map(PedidoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void actualizarEstado(String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException("El estado no puede estar vacio");
        }
        this.estado = nuevoEstado;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public UUID getRestauranteId() {
        return restauranteId;
    }

    public String getEstado() {
        return estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public List<PedidoItem> getItems() {
        return items;
    }
}
