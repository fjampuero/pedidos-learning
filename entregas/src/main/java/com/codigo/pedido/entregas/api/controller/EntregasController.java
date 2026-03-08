package com.codigo.pedido.entregas.api.controller;

import com.codigo.pedido.entregas.api.dto.ActualizarEstadoEntregaRequest;
import com.codigo.pedido.entregas.api.dto.EntregaResponse;
import com.codigo.pedido.entregas.api.dto.RegistrarEntregaRequest;
import com.codigo.pedido.entregas.domain.model.Entrega;
import com.codigo.pedido.entregas.domain.port.in.ActualizarEstadoEntregaUseCase;
import com.codigo.pedido.entregas.domain.port.in.ObtenerEntregaPorPedidoUseCase;
import com.codigo.pedido.entregas.domain.port.in.ObtenerEntregaUseCase;
import com.codigo.pedido.entregas.domain.port.in.RegistrarEntregaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/entregas")
@Tag(name = "Entregas", description = "Operaciones para registrar y gestionar entregas")
public class EntregasController {

    private static final Logger log = LoggerFactory.getLogger(EntregasController.class);

    private final RegistrarEntregaUseCase registrarEntrega;
    private final ObtenerEntregaUseCase obtenerEntrega;
    private final ObtenerEntregaPorPedidoUseCase obtenerEntregaPorPedido;
    private final ActualizarEstadoEntregaUseCase actualizarEstadoEntrega;

    public EntregasController(RegistrarEntregaUseCase registrarEntrega,
                              ObtenerEntregaUseCase obtenerEntrega,
                              ObtenerEntregaPorPedidoUseCase obtenerEntregaPorPedido,
                              ActualizarEstadoEntregaUseCase actualizarEstadoEntrega) {
        this.registrarEntrega = registrarEntrega;
        this.obtenerEntrega = obtenerEntrega;
        this.obtenerEntregaPorPedido = obtenerEntregaPorPedido;
        this.actualizarEstadoEntrega = actualizarEstadoEntrega;
    }

    @PostMapping
    @Operation(summary = "Registrar entrega")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entrega registrada"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida")
    })
    @PreAuthorize("hasAnyRole('ADMIN','REPARTIDOR')")
    public ResponseEntity<EntregaResponse> registrarEntrega(@RequestBody RegistrarEntregaRequest request) {
        log.info("Request para registrar entrega manual del pedido: {}", request.getPedidoId());
        Entrega entrega = registrarEntrega.registrar(
                request.getPedidoId(),
                request.getUsuarioId(),
                request.getDireccionEntrega(),
                request.getObservacion()
        );
        return ResponseEntity.ok(toResponse(entrega));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener entrega por id")
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN','REPARTIDOR')")
    public ResponseEntity<EntregaResponse> obtenerEntrega(@PathVariable UUID id) {
        log.info("Request para obtener entrega por id: {}", id);
        return ResponseEntity.ok(toResponse(obtenerEntrega.obtenerPorId(id)));
    }

    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Obtener entrega por pedido")
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN','REPARTIDOR')")
    public ResponseEntity<EntregaResponse> obtenerEntregaPorPedido(@PathVariable UUID pedidoId) {
        log.info("Request para obtener entrega por pedido: {}", pedidoId);
        return ResponseEntity.ok(toResponse(obtenerEntregaPorPedido.obtenerPorPedidoId(pedidoId)));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de entrega")
    @PreAuthorize("hasAnyRole('ADMIN','REPARTIDOR')")
    public ResponseEntity<EntregaResponse> actualizarEstado(@PathVariable UUID id,
                                                            @RequestBody ActualizarEstadoEntregaRequest request) {
        log.info("Request para actualizar entrega {} a estado {}", id, request.getEstado());
        return ResponseEntity.ok(toResponse(actualizarEstadoEntrega.actualizarEstado(id, request.getEstado())));
    }

    private EntregaResponse toResponse(Entrega entrega) {
        return new EntregaResponse(
                entrega.getId(),
                entrega.getPedidoId(),
                entrega.getUsuarioId(),
                entrega.getDireccionEntrega(),
                entrega.getRepartidor(),
                entrega.getEstado(),
                entrega.getObservacion(),
                entrega.getFechaCreacion(),
                entrega.getFechaEntrega()
        );
    }
}
