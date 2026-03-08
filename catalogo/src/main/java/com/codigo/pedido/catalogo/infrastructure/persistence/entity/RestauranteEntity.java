package com.codigo.pedido.catalogo.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "restaurantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestauranteEntity {

    @Id
    private UUID id;

    private String nombre;

    private String direccion;

    private String telefono;

    private Boolean activo;
}