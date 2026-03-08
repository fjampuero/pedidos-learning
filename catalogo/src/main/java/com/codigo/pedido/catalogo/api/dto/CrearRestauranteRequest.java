package com.codigo.pedido.catalogo.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearRestauranteRequest {

    private String nombre;
    private String direccion;
    private String telefono;
}