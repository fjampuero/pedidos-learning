package com.codigo.pedido.entregas.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Entrega {

    private final UUID id;
    private final UUID pedidoId;
    private final UUID usuarioId;
    private final String direccionEntrega;
    private final String repartidor;
    private String estado;
    private final String observacion;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaEntrega;

    public Entrega(UUID id,
                   UUID pedidoId,
                   UUID usuarioId,
                   String direccionEntrega,
                   String repartidor,
                   String estado,
                   String observacion,
                   LocalDateTime fechaCreacion,
                   LocalDateTime fechaEntrega) {
        if (direccionEntrega == null || direccionEntrega.isBlank()) {
            throw new IllegalArgumentException("La direccion de entrega es obligatoria");
        }

        this.id = id;
        this.pedidoId = pedidoId;
        this.usuarioId = usuarioId;
        this.direccionEntrega = direccionEntrega;
        this.repartidor = repartidor;
        this.estado = estado;
        this.observacion = observacion;
        this.fechaCreacion = fechaCreacion == null ? LocalDateTime.now() : fechaCreacion;
        this.fechaEntrega = fechaEntrega;
    }

    public void actualizarEstado(String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException("El estado de la entrega no puede estar vacio");
        }

        this.estado = nuevoEstado;
        if ("ENTREGADA".equalsIgnoreCase(nuevoEstado)) {
            this.fechaEntrega = LocalDateTime.now();
        }
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

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public String getRepartidor() {
        return repartidor;
    }

    public String getEstado() {
        return estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }
}
