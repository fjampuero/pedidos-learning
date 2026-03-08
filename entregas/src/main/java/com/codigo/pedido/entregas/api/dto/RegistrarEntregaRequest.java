package com.codigo.pedido.entregas.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RegistrarEntregaRequest {

    private UUID pedidoId;
    private UUID usuarioId;
    private String direccionEntrega;
    private String observacion;
}
