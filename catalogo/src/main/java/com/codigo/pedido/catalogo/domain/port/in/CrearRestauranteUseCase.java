package com.codigo.pedido.catalogo.domain.port.in;

import com.codigo.pedido.catalogo.domain.model.Restaurante;

public interface CrearRestauranteUseCase {

    Restaurante crear(Restaurante restaurante);
}