package com.codigo.pedido.payments.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class RegistrarPagoRequest {

    private UUID pedidoId;
    private BigDecimal monto;
    private String metodoPago;
}
