package com.codigo.pedido.payments.api.controller;

import com.codigo.pedido.payments.api.dto.PagoResponse;
import com.codigo.pedido.payments.api.dto.RegistrarPagoRequest;
import com.codigo.pedido.payments.domain.model.Pago;
import com.codigo.pedido.payments.domain.port.in.ObtenerPagoPorPedidoUseCase;
import com.codigo.pedido.payments.domain.port.in.ObtenerPagoUseCase;
import com.codigo.pedido.payments.domain.port.in.RegistrarPagoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Operaciones para registrar y consultar pagos")
public class PaymentsController {

    private static final Logger log = LoggerFactory.getLogger(PaymentsController.class);

    private final RegistrarPagoUseCase registrarPago;
    private final ObtenerPagoUseCase obtenerPago;
    private final ObtenerPagoPorPedidoUseCase obtenerPagoPorPedido;

    public PaymentsController(RegistrarPagoUseCase registrarPago,
                              ObtenerPagoUseCase obtenerPago,
                              ObtenerPagoPorPedidoUseCase obtenerPagoPorPedido) {
        this.registrarPago = registrarPago;
        this.obtenerPago = obtenerPago;
        this.obtenerPagoPorPedido = obtenerPagoPorPedido;
    }

    @PostMapping
    @Operation(summary = "Registrar pago", description = "Registra manualmente un pago para un pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida")
    })
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public ResponseEntity<PagoResponse> registrarPago(@RequestBody RegistrarPagoRequest request,
                                                      Authentication authentication) {
        UUID usuarioId = (UUID) authentication.getPrincipal();
        log.info("Request para registrar pago del pedido: {} por usuario: {}", request.getPedidoId(), usuarioId);
        Pago pago = registrarPago.registrar(
                request.getPedidoId(),
                usuarioId,
                request.getMonto(),
                request.getMetodoPago()
        );

        return ResponseEntity.ok(toResponse(pago));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public ResponseEntity<PagoResponse> obtenerPago(@PathVariable UUID id) {
        log.info("Request para obtener pago por id: {}", id);
        return ResponseEntity.ok(toResponse(obtenerPago.obtenerPorId(id)));
    }

    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Obtener pago por pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado para el pedido"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public ResponseEntity<PagoResponse> obtenerPagoPorPedido(@PathVariable UUID pedidoId) {
        log.info("Request para obtener pago por pedido: {}", pedidoId);
        return ResponseEntity.ok(toResponse(obtenerPagoPorPedido.obtenerPorPedidoId(pedidoId)));
    }

    private PagoResponse toResponse(Pago pago) {
        return new PagoResponse(
                pago.getId(),
                pago.getPedidoId(),
                pago.getUsuarioId(),
                pago.getMonto(),
                pago.getEstado(),
                pago.getMetodoPago(),
                pago.getReferenciaTransaccion(),
                pago.getFechaCreacion()
        );
    }
}
