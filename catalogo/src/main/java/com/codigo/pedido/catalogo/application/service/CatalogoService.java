package com.codigo.pedido.catalogo.application.service;

import com.codigo.pedido.catalogo.domain.model.Producto;
import com.codigo.pedido.catalogo.domain.model.Restaurante;
import com.codigo.pedido.catalogo.domain.port.in.*;
import com.codigo.pedido.catalogo.domain.port.out.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CatalogoService implements
        CrearRestauranteUseCase,
        ListarRestaurantesUseCase,
        CrearProductoUseCase,
        ListarProductosPorRestauranteUseCase,
        ObtenerProductoUseCase {

    private static final Logger log = LoggerFactory.getLogger(CatalogoService.class);

    private final RestauranteRepositoryPort restauranteRepository;
    private final ProductoRepositoryPort productoRepository;

    public CatalogoService(RestauranteRepositoryPort restauranteRepository,
                           ProductoRepositoryPort productoRepository) {
        this.restauranteRepository = restauranteRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public Restaurante crear(Restaurante restaurante) {
        log.info("Creando restaurante con id: {} y nombre: {}", restaurante.getId(), restaurante.getNombre());
        return restauranteRepository.guardar(restaurante);
    }

    @Override
    public List<Restaurante> listar() {
        log.info("Listando restaurantes");
        return restauranteRepository.listar();
    }

    @Override
    public Producto crear(Producto producto) {
        log.info("Creando producto con id: {} para restaurante: {}", producto.getId(), producto.getRestauranteId());
        return productoRepository.guardar(producto);
    }

    @Override
    public List<Producto> listar(UUID restauranteId) {
        log.info("Listando productos para restaurante: {}", restauranteId);
        return productoRepository.listarPorRestaurante(restauranteId);
    }

    @Override
    public Producto obtenerPorId(UUID id) {
        log.info("Consultando producto por id: {}", id);
        return productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }
}
