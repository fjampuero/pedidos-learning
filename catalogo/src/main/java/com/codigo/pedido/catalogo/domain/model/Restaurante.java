package com.codigo.pedido.catalogo.domain.model;

import java.util.UUID;

public class Restaurante {

    private final UUID id;
    private final String nombre;
    private final String direccion;
    private final String telefono;
    private boolean activo;

    public Restaurante(UUID id, String nombre, String direccion, String telefono) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.activo = true;
    }

    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public boolean isActivo() { return activo; }
}