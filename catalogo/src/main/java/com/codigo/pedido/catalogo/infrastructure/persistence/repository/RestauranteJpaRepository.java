package com.codigo.pedido.catalogo.infrastructure.persistence.repository;

import com.codigo.pedido.catalogo.infrastructure.persistence.entity.RestauranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestauranteJpaRepository extends JpaRepository<RestauranteEntity, UUID> {
}