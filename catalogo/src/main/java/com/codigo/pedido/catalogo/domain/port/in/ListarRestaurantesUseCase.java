package com.codigo.pedido.catalogo.domain.port.in;

import com.codigo.pedido.catalogo.domain.model.Restaurante;

import java.util.List;

public interface ListarRestaurantesUseCase {

    List<Restaurante> listar();
}