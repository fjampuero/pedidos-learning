package com.codigo.pedido.catalogo.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductoResponse {

    private UUID id;
    private UUID restauranteId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private boolean disponible;

    public ProductoResponse(UUID id, UUID restauranteId,
                            String nombre, String descripcion,
                            BigDecimal precio, boolean disponible) {

        this.id = id;
        this.restauranteId = restauranteId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponible = disponible;
    }

    public UUID getId() { return id; }
    public UUID getRestauranteId() { return restauranteId; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public boolean isDisponible() { return disponible; }
}