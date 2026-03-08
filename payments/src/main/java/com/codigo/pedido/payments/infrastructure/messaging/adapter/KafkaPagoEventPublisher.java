package com.codigo.pedido.payments.infrastructure.messaging.adapter;

import com.codigo.pedido.payments.domain.model.Pago;
import com.codigo.pedido.payments.domain.port.out.PagoEventPublisherPort;
import com.codigo.pedido.payments.infrastructure.messaging.event.PagoAprobadoEvent;
import com.codigo.pedido.payments.infrastructure.messaging.event.PagoRechazadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPagoEventPublisher implements PagoEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaPagoEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String pagoAprobadoTopic;
    private final String pagoRechazadoTopic;

    public KafkaPagoEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                                   @Value("${events.topics.pago-aprobado}") String pagoAprobadoTopic,
                                   @Value("${events.topics.pago-rechazado}") String pagoRechazadoTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.pagoAprobadoTopic = pagoAprobadoTopic;
        this.pagoRechazadoTopic = pagoRechazadoTopic;
    }

    @Override
    public void publicarPagoAprobado(Pago pago) {
        log.info("Publicando evento pago-aprobado para pedido: {} en topic: {}", pago.getPedidoId(), pagoAprobadoTopic);
        PagoAprobadoEvent event = new PagoAprobadoEvent(
                pago.getId(),
                pago.getPedidoId(),
                pago.getUsuarioId(),
                pago.getMonto(),
                pago.getEstado(),
                pago.getMetodoPago(),
                pago.getReferenciaTransaccion(),
                pago.getFechaCreacion()
        );
        kafkaTemplate.send(pagoAprobadoTopic, pago.getPedidoId().toString(), event);
    }

    @Override
    public void publicarPagoRechazado(Pago pago) {
        log.info("Publicando evento pago-rechazado para pedido: {} en topic: {}", pago.getPedidoId(), pagoRechazadoTopic);
        PagoRechazadoEvent event = new PagoRechazadoEvent(
                pago.getId(),
                pago.getPedidoId(),
                pago.getUsuarioId(),
                pago.getMonto(),
                pago.getEstado(),
                pago.getMetodoPago(),
                pago.getReferenciaTransaccion(),
                pago.getFechaCreacion()
        );
        kafkaTemplate.send(pagoRechazadoTopic, pago.getPedidoId().toString(), event);
    }
}
