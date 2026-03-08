package com.codigo.pedido.catalogo.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Producto {

    private final UUID id;
    private final UUID restauranteId;
    private final String nombre;
    private final String descripcion;
    private final BigDecimal precio;
    private boolean disponible;

    public Producto(UUID id, UUID restauranteId, String nombre, String descripcion, BigDecimal precio) {

        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        this.id = id;
        this.restauranteId = restauranteId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponible = true;
    }

    public UUID getId() { return id; }
    public UUID getRestauranteId() { return restauranteId; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public boolean isDisponible() { return disponible; }
}