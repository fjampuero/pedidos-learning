package com.codigo.pedido.orders.infrastructure.messaging.adapter;

import com.codigo.pedido.orders.domain.model.Pedido;
import com.codigo.pedido.orders.domain.port.out.PedidoEventPublisherPort;
import com.codigo.pedido.orders.infrastructure.messaging.event.PedidoCreadoEvent;
import com.codigo.pedido.orders.infrastructure.messaging.event.PedidoCreadoItemEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPedidoEventPublisher implements PedidoEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaPedidoEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String pedidoCreadoTopic;

    public KafkaPedidoEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                                     @Value("${events.topics.pedido-creado}") String pedidoCreadoTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.pedidoCreadoTopic = pedidoCreadoTopic;
    }

    @Override
    public void publicarPedidoCreado(Pedido pedido) {
        log.info("Publicando evento pedido-creado para pedido: {} en topic: {}", pedido.getId(), pedidoCreadoTopic);
        PedidoCreadoEvent event = new PedidoCreadoEvent(
                pedido.getId(),
                pedido.getUsuarioId(),
                pedido.getRestauranteId(),
                pedido.getEstado(),
                pedido.getTotal(),
                pedido.getFechaCreacion(),
                pedido.getItems().stream()
                        .map(item -> new PedidoCreadoItemEvent(
                                item.getProductoId(),
                                item.getNombreProducto(),
                                item.getPrecio(),
                                item.getCantidad(),
                                item.getSubtotal()
                        ))
                        .toList()
        );

        kafkaTemplate.send(pedidoCreadoTopic, pedido.getId().toString(), event);
    }
}
