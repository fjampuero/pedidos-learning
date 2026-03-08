package com.codigo.pedido.orders.infrastructure.persistence.adapter;

import com.codigo.pedido.orders.domain.model.Pedido;
import com.codigo.pedido.orders.domain.model.PedidoItem;
import com.codigo.pedido.orders.domain.port.out.PedidoRepositoryPort;
import com.codigo.pedido.orders.infrastructure.persistence.entity.PedidoEntity;
import com.codigo.pedido.orders.infrastructure.persistence.entity.PedidoItemEntity;
import com.codigo.pedido.orders.infrastructure.persistence.repository.PedidoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PedidoJpaAdapter implements PedidoRepositoryPort {

    private final PedidoJpaRepository repository;

    public PedidoJpaAdapter(PedidoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        PedidoEntity entity = toEntity(pedido);
        PedidoEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Pedido> buscarPorId(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Pedido> listarTodos() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Pedido> listarPorUsuario(UUID usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Pedido> listarPorEstado(String estado) {
        return repository.findByEstado(estado).stream().map(this::toDomain).toList();
    }

    @Override
    public void eliminar(UUID id) {
        repository.deleteById(id);
    }

    private PedidoEntity toEntity(Pedido pedido) {
        PedidoEntity entity = PedidoEntity.builder()
                .id(pedido.getId())
                .usuarioId(pedido.getUsuarioId())
                .restauranteId(pedido.getRestauranteId())
                .estado(pedido.getEstado())
                .total(pedido.getTotal())
                .fechaCreacion(pedido.getFechaCreacion())
                .build();

        List<PedidoItemEntity> itemEntities = pedido.getItems().stream()
                .map(item -> PedidoItemEntity.builder()
                        .id(item.getId())
                        .pedido(entity)
                        .productoId(item.getProductoId())
                        .nombreProducto(item.getNombreProducto())
                        .precio(item.getPrecio())
                        .cantidad(item.getCantidad())
                        .subtotal(item.getSubtotal())
                        .build())
                .toList();

        entity.setItems(itemEntities);
        return entity;
    }

    private Pedido toDomain(PedidoEntity entity) {
        List<PedidoItem> items = entity.getItems().stream()
                .map(item -> new PedidoItem(
                        item.getId(),
                        entity.getId(),
                        item.getProductoId(),
                        item.getNombreProducto(),
                        item.getPrecio(),
                        item.getCantidad()
                ))
                .toList();

        return new Pedido(
                entity.getId(),
                entity.getUsuarioId(),
                entity.getRestauranteId(),
                entity.getEstado(),
                entity.getFechaCreacion(),
                items
        );
    }
}
