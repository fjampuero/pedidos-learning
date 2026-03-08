package com.codigo.pedido.catalogo.infrastructure.persistence.adapter;

import com.codigo.pedido.catalogo.domain.model.Producto;
import com.codigo.pedido.catalogo.domain.port.out.ProductoRepositoryPort;
import com.codigo.pedido.catalogo.infrastructure.persistence.entity.ProductoEntity;
import com.codigo.pedido.catalogo.infrastructure.persistence.repository.ProductoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductoJpaAdapter implements ProductoRepositoryPort {

    private final ProductoJpaRepository repository;

    public ProductoJpaAdapter(ProductoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Producto guardar(Producto producto) {

        ProductoEntity entity = ProductoEntity.builder()
                .id(producto.getId())
                .restauranteId(producto.getRestauranteId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .disponible(producto.isDisponible())
                .build();

        ProductoEntity saved = repository.save(entity);

        return new Producto(
                saved.getId(),
                saved.getRestauranteId(),
                saved.getNombre(),
                saved.getDescripcion(),
                saved.getPrecio()
        );
    }

    @Override
    public List<Producto> listarPorRestaurante(UUID restauranteId) {

        return repository.findByRestauranteId(restauranteId)
                .stream()
                .map(entity -> new Producto(
                        entity.getId(),
                        entity.getRestauranteId(),
                        entity.getNombre(),
                        entity.getDescripcion(),
                        entity.getPrecio()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Producto> buscarPorId(UUID id) {
        return repository.findById(id)
                .map(entity -> new Producto(
                        entity.getId(),
                        entity.getRestauranteId(),
                        entity.getNombre(),
                        entity.getDescripcion(),
                        entity.getPrecio()
                ));
    }
}
