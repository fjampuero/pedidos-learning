package com.codigo.pedido.entregas.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "entregas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntregaEntity {

    @Id
    private UUID id;

    private UUID pedidoId;

    private UUID usuarioId;

    private String direccionEntrega;

    private String repartidor;

    private String estado;

    private String observacion;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaEntrega;
}
