package com.codigo.pedido.entregas.infrastructure.persistence.repository;

import com.codigo.pedido.entregas.infrastructure.persistence.entity.EntregaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EntregaJpaRepository extends JpaRepository<EntregaEntity, UUID> {

    Optional<EntregaEntity> findByPedidoId(UUID pedidoId);
}
