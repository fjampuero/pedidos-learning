package com.codigo.pedido.payments.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoEntity {

    @Id
    private UUID id;

    private UUID pedidoId;

    private UUID usuarioId;

    private BigDecimal monto;

    private String estado;

    private String metodoPago;

    private String referenciaTransaccion;

    private LocalDateTime fechaCreacion;
}
