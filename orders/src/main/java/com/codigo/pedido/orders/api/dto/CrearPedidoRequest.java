package com.codigo.pedido.orders.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CrearPedidoRequest {

    private UUID restauranteId;
    private List<CrearPedidoItemRequest> items;
}
