package com.codigo.pedido.orders.application.service;

import com.codigo.pedido.orders.domain.model.Pedido;
import com.codigo.pedido.orders.domain.model.PedidoItem;
import com.codigo.pedido.orders.domain.port.in.ActualizarEstadoPedidoUseCase;
import com.codigo.pedido.orders.domain.port.in.AplicarPagoAprobadoUseCase;
import com.codigo.pedido.orders.domain.port.in.CrearPedidoItemCommand;
import com.codigo.pedido.orders.domain.port.in.CrearPedidoUseCase;
import com.codigo.pedido.orders.domain.port.in.EliminarPedidoUseCase;
import com.codigo.pedido.orders.domain.port.in.ListarPedidosUseCase;
import com.codigo.pedido.orders.domain.port.in.ObtenerPedidoUseCase;
import com.codigo.pedido.orders.domain.port.out.CatalogoClientPort;
import com.codigo.pedido.orders.domain.port.out.PedidoEventPublisherPort;
import com.codigo.pedido.orders.domain.port.out.PedidoRepositoryPort;
import com.codigo.pedido.orders.domain.port.out.ProductoCatalogo;
import com.codigo.pedido.orders.domain.port.out.UsuarioClientPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrdersService implements
        CrearPedidoUseCase,
        ObtenerPedidoUseCase,
        ListarPedidosUseCase,
        ActualizarEstadoPedidoUseCase,
        AplicarPagoAprobadoUseCase,
        EliminarPedidoUseCase {

    private static final Logger log = LoggerFactory.getLogger(OrdersService.class);

    private final PedidoRepositoryPort pedidoRepository;
    private final UsuarioClientPort usuarioClient;
    private final CatalogoClientPort catalogoClient;
    private final PedidoEventPublisherPort pedidoEventPublisher;

    public OrdersService(PedidoRepositoryPort pedidoRepository,
                         UsuarioClientPort usuarioClient,
                         CatalogoClientPort catalogoClient,
                         PedidoEventPublisherPort pedidoEventPublisher) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioClient = usuarioClient;
        this.catalogoClient = catalogoClient;
        this.pedidoEventPublisher = pedidoEventPublisher;
    }

    @Override
    public Pedido crear(UUID usuarioId, UUID restauranteId, List<CrearPedidoItemCommand> items) {
        log.info("Creando pedido para usuario: {} en restaurante: {} con {} item(s)", usuarioId, restauranteId, items.size());
        usuarioClient.validarUsuario(usuarioId);

        UUID pedidoId = UUID.randomUUID();

        List<PedidoItem> pedidoItems = items.stream()
                .map(item -> construirPedidoItem(pedidoId, restauranteId, item))
                .toList();

        Pedido pedido = new Pedido(
                pedidoId,
                usuarioId,
                restauranteId,
                "CREADO",
                null,
                pedidoItems
        );

        Pedido pedidoGuardado = pedidoRepository.guardar(pedido);
        log.info("Pedido creado con id: {} y total: {}", pedidoGuardado.getId(), pedidoGuardado.getTotal());
        pedidoEventPublisher.publicarPedidoCreado(pedidoGuardado);
        return pedidoGuardado;
    }

    @Override
    public Pedido obtenerPorId(UUID id) {
        log.info("Consultando pedido por id: {}", id);
        return pedidoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
    }

    @Override
    public List<Pedido> listar(UUID usuarioId, String estado) {
        log.info("Listando pedidos con filtros usuarioId: {} y estado: {}", usuarioId, estado);
        if (usuarioId != null) {
            return pedidoRepository.listarPorUsuario(usuarioId);
        }

        if (estado != null && !estado.isBlank()) {
            return pedidoRepository.listarPorEstado(estado);
        }

        return pedidoRepository.listarTodos();
    }

    @Override
    public Pedido actualizarEstado(UUID id, String estado) {
        log.info("Actualizando estado del pedido {} a {}", id, estado);
        Pedido pedido = obtenerPorId(id);
        pedido.actualizarEstado(estado);
        return pedidoRepository.guardar(pedido);
    }

    @Override
    public Pedido aplicarPagoAprobado(UUID pedidoId) {
        log.info("Aplicando pago aprobado al pedido: {}", pedidoId);
        Pedido pedido = obtenerPorId(pedidoId);
        pedido.actualizarEstado("PAGADO");
        return pedidoRepository.guardar(pedido);
    }

    @Override
    public void eliminar(UUID id) {
        log.info("Eliminando pedido con id: {}", id);
        pedidoRepository.eliminar(id);
    }

    private PedidoItem construirPedidoItem(UUID pedidoId,
                                           UUID restauranteId,
                                           CrearPedidoItemCommand item) {
        log.info("Construyendo item de pedido para pedido: {} con producto: {} y cantidad: {}",
                pedidoId, item.productoId(), item.cantidad());
        ProductoCatalogo producto = catalogoClient.obtenerProducto(item.productoId());

        if (!producto.disponible()) {
            log.warn("Producto no disponible para pedido {}: {}", pedidoId, item.productoId());
            throw new IllegalArgumentException("Producto no disponible");
        }

        if (!producto.restauranteId().equals(restauranteId)) {
            log.warn("Producto {} no pertenece al restaurante {}", item.productoId(), restauranteId);
            throw new IllegalArgumentException("El producto no pertenece al restaurante indicado");
        }

        return new PedidoItem(
                UUID.randomUUID(),
                pedidoId,
                producto.id(),
                producto.nombre(),
                producto.precio(),
                item.cantidad()
        );
    }
}
