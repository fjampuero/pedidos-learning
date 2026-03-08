package com.codigo.pedido.payments.infrastructure.messaging.consumer;

import com.codigo.pedido.payments.domain.port.in.RegistrarPagoDesdePedidoUseCase;
import com.codigo.pedido.payments.infrastructure.messaging.event.PedidoCreadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoCreadoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PedidoCreadoConsumer.class);

    private final RegistrarPagoDesdePedidoUseCase registrarPagoDesdePedido;

    public PedidoCreadoConsumer(RegistrarPagoDesdePedidoUseCase registrarPagoDesdePedido) {
        this.registrarPagoDesdePedido = registrarPagoDesdePedido;
    }

    @KafkaListener(
            topics = "${events.topics.pedido-creado}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumirPedidoCreado(PedidoCreadoEvent event) {
        log.info("Evento pedido-creado recibido para pedido: {} y usuario: {}", event.pedidoId(), event.usuarioId());
        registrarPagoDesdePedido.registrarDesdePedido(
                event.pedidoId(),
                event.usuarioId(),
                event.total()
        );
    }
}
