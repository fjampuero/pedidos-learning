package com.codigo.pedido.payments.infrastructure.persistence.adapter;

import com.codigo.pedido.payments.domain.model.Pago;
import com.codigo.pedido.payments.domain.port.out.PagoRepositoryPort;
import com.codigo.pedido.payments.infrastructure.persistence.entity.PagoEntity;
import com.codigo.pedido.payments.infrastructure.persistence.repository.PagoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PagoJpaAdapter implements PagoRepositoryPort {

    private final PagoJpaRepository repository;

    public PagoJpaAdapter(PagoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pago guardar(Pago pago) {
        PagoEntity saved = repository.save(toEntity(pago));
        return toDomain(saved);
    }

    @Override
    public Optional<Pago> buscarPorId(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Pago> buscarPorPedidoId(UUID pedidoId) {
        return repository.findByPedidoId(pedidoId).map(this::toDomain);
    }

    private PagoEntity toEntity(Pago pago) {
        return PagoEntity.builder()
                .id(pago.getId())
                .pedidoId(pago.getPedidoId())
                .usuarioId(pago.getUsuarioId())
                .monto(pago.getMonto())
                .estado(pago.getEstado())
                .metodoPago(pago.getMetodoPago())
                .referenciaTransaccion(pago.getReferenciaTransaccion())
                .fechaCreacion(pago.getFechaCreacion())
                .build();
    }

    private Pago toDomain(PagoEntity entity) {
        return new Pago(
                entity.getId(),
                entity.getPedidoId(),
                entity.getUsuarioId(),
                entity.getMonto(),
                entity.getEstado(),
                entity.getMetodoPago(),
                entity.getReferenciaTransaccion(),
                entity.getFechaCreacion()
        );
    }
}
