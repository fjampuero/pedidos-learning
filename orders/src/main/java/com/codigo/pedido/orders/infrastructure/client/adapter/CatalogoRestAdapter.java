package com.codigo.pedido.orders.infrastructure.client.adapter;

import com.codigo.pedido.orders.domain.port.out.CatalogoClientPort;
import com.codigo.pedido.orders.domain.port.out.ProductoCatalogo;
import com.codigo.pedido.orders.infrastructure.client.dto.ProductoClientResponse;
import com.codigo.pedido.orders.infrastructure.resilience.SimpleCircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class CatalogoRestAdapter implements CatalogoClientPort {

    private static final Logger log = LoggerFactory.getLogger(CatalogoRestAdapter.class);

    private final RestClient restClient;
    private final SimpleCircuitBreaker circuitBreaker;

    public CatalogoRestAdapter(RestClient.Builder builder,
                               SimpleCircuitBreaker circuitBreaker,
                               @Value("${integrations.catalogo.base-url}") String catalogoBaseUrl) {
        this.restClient = builder.baseUrl(catalogoBaseUrl).build();
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public ProductoCatalogo obtenerProducto(UUID productoId) {
        log.info("Consultando producto en ms catalogo con id: {}", productoId);
        ProductoClientResponse producto = circuitBreaker.execute("catalogo", () -> restClient.get()
                .uri("/api/catalogo/productos/{id}", productoId)
                .retrieve()
                .body(ProductoClientResponse.class));

        if (producto == null) {
            log.warn("Producto no encontrado en catalogo: {}", productoId);
            throw new IllegalArgumentException("Producto no encontrado");
        }

        log.info("Producto obtenido correctamente desde catalogo: {}", productoId);

        return new ProductoCatalogo(
                producto.id(),
                producto.restauranteId(),
                producto.nombre(),
                producto.precio(),
                producto.disponible()
        );
    }
}
