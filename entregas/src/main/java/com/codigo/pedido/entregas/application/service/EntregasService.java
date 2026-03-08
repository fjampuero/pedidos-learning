package com.codigo.pedido.entregas.application.service;

import com.codigo.pedido.entregas.domain.model.Entrega;
import com.codigo.pedido.entregas.domain.port.in.ActualizarEstadoEntregaUseCase;
import com.codigo.pedido.entregas.domain.port.in.ObtenerEntregaPorPedidoUseCase;
import com.codigo.pedido.entregas.domain.port.in.ObtenerEntregaUseCase;
import com.codigo.pedido.entregas.domain.port.in.RegistrarEntregaDesdePagoUseCase;
import com.codigo.pedido.entregas.domain.port.in.RegistrarEntregaUseCase;
import com.codigo.pedido.entregas.domain.port.out.EntregaEventPublisherPort;
import com.codigo.pedido.entregas.domain.port.out.EntregaRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EntregasService implements
        RegistrarEntregaUseCase,
        RegistrarEntregaDesdePagoUseCase,
        ObtenerEntregaUseCase,
        ObtenerEntregaPorPedidoUseCase,
        ActualizarEstadoEntregaUseCase {

    private static final Logger log = LoggerFactory.getLogger(EntregasService.class);

    private static final String DIRECCION_PENDIENTE = "DIRECCION PENDIENTE";

    private final EntregaRepositoryPort entregaRepository;
    private final EntregaEventPublisherPort entregaEventPublisher;

    public EntregasService(EntregaRepositoryPort entregaRepository,
                           EntregaEventPublisherPort entregaEventPublisher) {
        this.entregaRepository = entregaRepository;
        this.entregaEventPublisher = entregaEventPublisher;
    }

    @Override
    public Entrega registrar(UUID pedidoId, UUID usuarioId, String direccionEntrega, String observacion) {
        log.info("Registrando entrega manual para pedido: {} y usuario: {}", pedidoId, usuarioId);
        if (entregaRepository.buscarPorPedidoId(pedidoId).isPresent()) {
            log.warn("Intento de registrar entrega duplicada para pedido: {}", pedidoId);
            throw new IllegalArgumentException("Ya existe una entrega para este pedido");
        }

        Entrega entrega = new Entrega(
                UUID.randomUUID(),
                pedidoId,
                usuarioId,
                direccionEntrega,
                null,
                "PENDIENTE",
                observacion,
                null,
                null
        );

        Entrega guardada = entregaRepository.guardar(entrega);
        log.info("Entrega registrada con id: {} para pedido: {}", guardada.getId(), guardada.getPedidoId());
        entregaEventPublisher.publicarEntregaCreada(guardada);
        return guardada;
    }

    @Override
    public Entrega registrarDesdePago(UUID pedidoId, UUID usuarioId) {
        log.info("Registrando entrega automatica tras pago aprobado para pedido: {}", pedidoId);
        return entregaRepository.buscarPorPedidoId(pedidoId)
                .orElseGet(() -> {
                    Entrega entrega = new Entrega(
                            UUID.randomUUID(),
                            pedidoId,
                            usuarioId,
                            DIRECCION_PENDIENTE,
                            null,
                            "PENDIENTE",
                            "Creada automaticamente tras pago aprobado",
                            null,
                            null
                    );

                    Entrega guardada = entregaRepository.guardar(entrega);
                    log.info("Entrega automatica registrada con id: {} para pedido: {}", guardada.getId(), guardada.getPedidoId());
                    entregaEventPublisher.publicarEntregaCreada(guardada);
                    return guardada;
                });
    }

    @Override
    public Entrega obtenerPorId(UUID id) {
        log.info("Consultando entrega por id: {}", id);
        return entregaRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Entrega no encontrada"));
    }

    @Override
    public Entrega obtenerPorPedidoId(UUID pedidoId) {
        log.info("Consultando entrega por pedido: {}", pedidoId);
        return entregaRepository.buscarPorPedidoId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Entrega no encontrada para el pedido"));
    }

    @Override
    public Entrega actualizarEstado(UUID id, String estado) {
        log.info("Actualizando entrega {} a estado {}", id, estado);
        Entrega entrega = obtenerPorId(id);
        entrega.actualizarEstado(estado);
        Entrega guardada = entregaRepository.guardar(entrega);
        entregaEventPublisher.publicarEntregaActualizada(guardada);
        return guardada;
    }
}
