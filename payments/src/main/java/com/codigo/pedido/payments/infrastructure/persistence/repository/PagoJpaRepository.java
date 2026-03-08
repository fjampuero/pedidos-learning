package com.codigo.pedido.payments.infrastructure.persistence.repository;

import com.codigo.pedido.payments.infrastructure.persistence.entity.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PagoJpaRepository extends JpaRepository<PagoEntity, UUID> {

    Optional<PagoEntity> findByPedidoId(UUID pedidoId);
}
