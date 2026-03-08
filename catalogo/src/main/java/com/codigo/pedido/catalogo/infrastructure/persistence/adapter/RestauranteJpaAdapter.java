package com.codigo.pedido.catalogo.infrastructure.persistence.adapter;

import com.codigo.pedido.catalogo.domain.model.Restaurante;
import com.codigo.pedido.catalogo.domain.port.out.RestauranteRepositoryPort;
import com.codigo.pedido.catalogo.infrastructure.persistence.entity.RestauranteEntity;
import com.codigo.pedido.catalogo.infrastructure.persistence.repository.RestauranteJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestauranteJpaAdapter implements RestauranteRepositoryPort {

    private final RestauranteJpaRepository repository;

    public RestauranteJpaAdapter(RestauranteJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Restaurante guardar(Restaurante restaurante) {

        RestauranteEntity entity = RestauranteEntity.builder()
                .id(restaurante.getId())
                .nombre(restaurante.getNombre())
                .direccion(restaurante.getDireccion())
                .telefono(restaurante.getTelefono())
                .activo(restaurante.isActivo())
                .build();

        RestauranteEntity saved = repository.save(entity);

        return new Restaurante(
                saved.getId(),
                saved.getNombre(),
                saved.getDireccion(),
                saved.getTelefono()
        );
    }

    @Override
    public List<Restaurante> listar() {

        return repository.findAll()
                .stream()
                .map(entity -> new Restaurante(
                        entity.getId(),
                        entity.getNombre(),
                        entity.getDireccion(),
                        entity.getTelefono()
                ))
                .collect(Collectors.toList());
    }
}