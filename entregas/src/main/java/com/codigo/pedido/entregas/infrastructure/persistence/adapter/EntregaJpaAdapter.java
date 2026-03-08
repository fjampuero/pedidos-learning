package com.codigo.pedido.entregas.infrastructure.persistence.adapter;

import com.codigo.pedido.entregas.domain.model.Entrega;
import com.codigo.pedido.entregas.domain.port.out.EntregaRepositoryPort;
import com.codigo.pedido.entregas.infrastructure.persistence.entity.EntregaEntity;
import com.codigo.pedido.entregas.infrastructure.persistence.repository.EntregaJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class EntregaJpaAdapter implements EntregaRepositoryPort {

    private final EntregaJpaRepository repository;

    public EntregaJpaAdapter(EntregaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Entrega guardar(Entrega entrega) {
        EntregaEntity saved = repository.save(toEntity(entrega));
        return toDomain(saved);
    }

    @Override
    public Optional<Entrega> buscarPorId(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Entrega> buscarPorPedidoId(UUID pedidoId) {
        return repository.findByPedidoId(pedidoId).map(this::toDomain);
    }

    private EntregaEntity toEntity(Entrega entrega) {
        return EntregaEntity.builder()
                .id(entrega.getId())
                .pedidoId(entrega.getPedidoId())
                .usuarioId(entrega.getUsuarioId())
                .direccionEntrega(entrega.getDireccionEntrega())
                .repartidor(entrega.getRepartidor())
                .estado(entrega.getEstado())
                .observacion(entrega.getObservacion())
                .fechaCreacion(entrega.getFechaCreacion())
                .fechaEntrega(entrega.getFechaEntrega())
                .build();
    }

    private Entrega toDomain(EntregaEntity entity) {
        return new Entrega(
                entity.getId(),
                entity.getPedidoId(),
                entity.getUsuarioId(),
                entity.getDireccionEntrega(),
                entity.getRepartidor(),
                entity.getEstado(),
                entity.getObservacion(),
                entity.getFechaCreacion(),
                entity.getFechaEntrega()
        );
    }
}
