package com.codigo.pedido.catalogo.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CrearProductoRequest {

    private UUID restauranteId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
}