package com.codigo.pedido.catalogo.infrastructure.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;
@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoEntity {

    @Id
    private UUID id;

    private UUID restauranteId;

    private String nombre;

    private String descripcion;

    private BigDecimal precio;

    private Boolean disponible;
}
