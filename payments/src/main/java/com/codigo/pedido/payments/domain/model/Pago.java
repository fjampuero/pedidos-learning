package com.codigo.pedido.payments.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Pago {

    private final UUID id;
    private final UUID pedidoId;
    private final UUID usuarioId;
    private final BigDecimal monto;
    private String estado;
    private final String metodoPago;
    private final String referenciaTransaccion;
    private final LocalDateTime fechaCreacion;

    public Pago(UUID id,
                UUID pedidoId,
                UUID usuarioId,
                BigDecimal monto,
                String estado,
                String metodoPago,
                String referenciaTransaccion,
                LocalDateTime fechaCreacion) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero");
        }

        this.id = id;
        this.pedidoId = pedidoId;
        this.usuarioId = usuarioId;
        this.monto = monto;
        this.estado = estado;
        this.metodoPago = metodoPago;
        this.referenciaTransaccion = referenciaTransaccion;
        this.fechaCreacion = fechaCreacion == null ? LocalDateTime.now() : fechaCreacion;
    }

    public void actualizarEstado(String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException("El estado del pago no puede estar vacio");
        }
        this.estado = nuevoEstado;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPedidoId() {
        return pedidoId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getEstado() {
        return estado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public String getReferenciaTransaccion() {
        return referenciaTransaccion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}
