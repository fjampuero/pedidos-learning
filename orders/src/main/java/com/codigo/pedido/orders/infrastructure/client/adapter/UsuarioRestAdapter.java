package com.codigo.pedido.orders.infrastructure.client.adapter;

import com.codigo.pedido.orders.domain.port.out.UsuarioClientPort;
import com.codigo.pedido.orders.infrastructure.client.dto.UsuarioClientResponse;
import com.codigo.pedido.orders.infrastructure.resilience.SimpleCircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class UsuarioRestAdapter implements UsuarioClientPort {

    private static final Logger log = LoggerFactory.getLogger(UsuarioRestAdapter.class);

    private final RestClient restClient;
    private final SimpleCircuitBreaker circuitBreaker;

    public UsuarioRestAdapter(RestClient.Builder builder,
                              SimpleCircuitBreaker circuitBreaker,
                              @Value("${integrations.usuarios.base-url}") String usuariosBaseUrl) {
        this.restClient = builder.baseUrl(usuariosBaseUrl).build();
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public void validarUsuario(UUID usuarioId) {
        log.info("Validando usuario en ms usuarios con id: {}", usuarioId);
        UsuarioClientResponse usuario = circuitBreaker.execute("usuarios", () -> restClient.get()
                .uri("/api/usuarios/{id}", usuarioId)
                .retrieve()
                .body(UsuarioClientResponse.class));

        if (usuario == null || !usuario.activo()) {
            log.warn("Usuario invalido o inactivo para pedido: {}", usuarioId);
            throw new IllegalArgumentException("Usuario no valido para crear pedidos");
        }

        log.info("Usuario validado correctamente: {}", usuarioId);
    }
}
