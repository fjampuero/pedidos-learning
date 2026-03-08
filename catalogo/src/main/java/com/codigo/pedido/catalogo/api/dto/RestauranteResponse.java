package com.codigo.pedido.catalogo.api.dto;

import java.util.UUID;

public class RestauranteResponse {

    private UUID id;
    private String nombre;
    private String direccion;
    private String telefono;
    private boolean activo;

    public RestauranteResponse(UUID id, String nombre, String direccion,
                               String telefono, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.activo = activo;
    }

    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public boolean isActivo() { return activo; }
}