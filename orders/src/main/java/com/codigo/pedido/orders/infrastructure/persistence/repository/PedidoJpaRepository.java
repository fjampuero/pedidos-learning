package com.codigo.pedido.orders.infrastructure.persistence.repository;

import com.codigo.pedido.orders.infrastructure.persistence.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, UUID> {

    List<PedidoEntity> findByUsuarioId(UUID usuarioId);

    List<PedidoEntity> findByEstado(String estado);
}
