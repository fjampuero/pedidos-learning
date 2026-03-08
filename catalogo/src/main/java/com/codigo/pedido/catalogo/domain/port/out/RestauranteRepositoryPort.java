package com.codigo.pedido.catalogo.domain.port.out;

import com.codigo.pedido.catalogo.domain.model.Restaurante;

import java.util.List;

public interface RestauranteRepositoryPort {

    Restaurante guardar(Restaurante restaurante);

    List<Restaurante> listar();
}