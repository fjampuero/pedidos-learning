package com.codigo.pedido.orders.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CrearPedidoItemRequest {

    private UUID productoId;
    private int cantidad;
}
