package com.codigo.pedido.catalogo.infrastructure.persistence.repository;

import com.codigo.pedido.catalogo.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductoJpaRepository extends JpaRepository<ProductoEntity, UUID> {

    List<ProductoEntity> findByRestauranteId(UUID restauranteId);
}
