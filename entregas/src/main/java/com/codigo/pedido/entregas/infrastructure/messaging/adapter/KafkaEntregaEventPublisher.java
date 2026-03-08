package com.codigo.pedido.entregas.infrastructure.messaging.adapter;

import com.codigo.pedido.entregas.domain.model.Entrega;
import com.codigo.pedido.entregas.domain.port.out.EntregaEventPublisherPort;
import com.codigo.pedido.entregas.infrastructure.messaging.event.EntregaActualizadaEvent;
import com.codigo.pedido.entregas.infrastructure.messaging.event.EntregaCreadaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEntregaEventPublisher implements EntregaEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaEntregaEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String entregaCreadaTopic;
    private final String entregaActualizadaTopic;

    public KafkaEntregaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                                      @Value("${events.topics.entrega-creada}") String entregaCreadaTopic,
                                      @Value("${events.topics.entrega-actualizada}") String entregaActualizadaTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.entregaCreadaTopic = entregaCreadaTopic;
        this.entregaActualizadaTopic = entregaActualizadaTopic;
    }

    @Override
    public void publicarEntregaCreada(Entrega entrega) {
        log.info("Publicando evento entrega-creada para pedido: {} en topic: {}", entrega.getPedidoId(), entregaCreadaTopic);
        EntregaCreadaEvent event = new EntregaCreadaEvent(
                entrega.getId(),
                entrega.getPedidoId(),
                entrega.getUsuarioId(),
                entrega.getDireccionEntrega(),
                entrega.getEstado(),
                entrega.getObservacion(),
                entrega.getFechaCreacion()
        );
        kafkaTemplate.send(entregaCreadaTopic, entrega.getPedidoId().toString(), event);
    }

    @Override
    public void publicarEntregaActualizada(Entrega entrega) {
        log.info("Publicando evento entrega-actualizada para pedido: {} en topic: {}", entrega.getPedidoId(), entregaActualizadaTopic);
        EntregaActualizadaEvent event = new EntregaActualizadaEvent(
                entrega.getId(),
                entrega.getPedidoId(),
                entrega.getUsuarioId(),
                entrega.getEstado(),
                entrega.getFechaEntrega()
        );
        kafkaTemplate.send(entregaActualizadaTopic, entrega.getPedidoId().toString(), event);
    }
}
