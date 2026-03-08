package com.codigo.pedido.payments.application.service;

import com.codigo.pedido.payments.domain.model.Pago;
import com.codigo.pedido.payments.domain.port.in.ObtenerPagoPorPedidoUseCase;
import com.codigo.pedido.payments.domain.port.in.ObtenerPagoUseCase;
import com.codigo.pedido.payments.domain.port.in.RegistrarPagoDesdePedidoUseCase;
import com.codigo.pedido.payments.domain.port.in.RegistrarPagoUseCase;
import com.codigo.pedido.payments.domain.port.out.PagoEventPublisherPort;
import com.codigo.pedido.payments.domain.port.out.PagoRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentsService implements
        RegistrarPagoUseCase,
        RegistrarPagoDesdePedidoUseCase,
        ObtenerPagoUseCase,
        ObtenerPagoPorPedidoUseCase {

    private static final Logger log = LoggerFactory.getLogger(PaymentsService.class);

    private final PagoRepositoryPort pagoRepository;
    private final PagoEventPublisherPort pagoEventPublisher;

    public PaymentsService(PagoRepositoryPort pagoRepository,
                           PagoEventPublisherPort pagoEventPublisher) {
        this.pagoRepository = pagoRepository;
        this.pagoEventPublisher = pagoEventPublisher;
    }

    @Override
    public Pago registrar(UUID pedidoId, UUID usuarioId, BigDecimal monto, String metodoPago) {
        log.info("Registrando pago manual para pedido: {} y usuario: {} con metodo: {}", pedidoId, usuarioId, metodoPago);
        if (pagoRepository.buscarPorPedidoId(pedidoId).isPresent()) {
            log.warn("Intento de registrar pago duplicado para pedido: {}", pedidoId);
            throw new IllegalArgumentException("Ya existe un pago registrado para este pedido");
        }

        Pago pago = new Pago(
                UUID.randomUUID(),
                pedidoId,
                usuarioId,
                monto,
                "APROBADO",
                metodoPago,
                generarReferencia(),
                null
        );

        Pago guardado = pagoRepository.guardar(pago);
        log.info("Pago registrado con id: {} para pedido: {}", guardado.getId(), guardado.getPedidoId());
        pagoEventPublisher.publicarPagoAprobado(guardado);
        return guardado;
    }

    @Override
    public Pago registrarDesdePedido(UUID pedidoId, UUID usuarioId, BigDecimal monto) {
        log.info("Registrando pago automatico para pedido: {} y usuario: {}", pedidoId, usuarioId);
        if (pagoRepository.buscarPorPedidoId(pedidoId).isPresent()) {
            log.info("Pago ya existente para pedido: {}, se reutiliza registro actual", pedidoId);
            return pagoRepository.buscarPorPedidoId(pedidoId)
                    .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado"));
        }

        Pago pago = new Pago(
                UUID.randomUUID(),
                pedidoId,
                usuarioId,
                monto,
                "APROBADO",
                "AUTO",
                generarReferencia(),
                null
        );

        Pago guardado = pagoRepository.guardar(pago);
        log.info("Pago automatico registrado con id: {} para pedido: {}", guardado.getId(), guardado.getPedidoId());
        pagoEventPublisher.publicarPagoAprobado(guardado);
        return guardado;
    }

    @Override
    public Pago obtenerPorId(UUID id) {
        log.info("Consultando pago por id: {}", id);
        return pagoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado"));
    }

    @Override
    public Pago obtenerPorPedidoId(UUID pedidoId) {
        log.info("Consultando pago por pedido: {}", pedidoId);
        return pagoRepository.buscarPorPedidoId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado para el pedido"));
    }

    private String generarReferencia() {
        return "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
