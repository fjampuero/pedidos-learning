package com.codigo.pedido.orders.api.controller;

import com.codigo.pedido.orders.api.dto.ActualizarEstadoPedidoRequest;
import com.codigo.pedido.orders.api.dto.CrearPedidoRequest;
import com.codigo.pedido.orders.api.dto.PedidoItemResponse;
import com.codigo.pedido.orders.api.dto.PedidoResponse;
import com.codigo.pedido.orders.domain.model.Pedido;
import com.codigo.pedido.orders.domain.port.in.ActualizarEstadoPedidoUseCase;
import com.codigo.pedido.orders.domain.port.in.CrearPedidoItemCommand;
import com.codigo.pedido.orders.domain.port.in.CrearPedidoUseCase;
import com.codigo.pedido.orders.domain.port.in.EliminarPedidoUseCase;
import com.codigo.pedido.orders.domain.port.in.ListarPedidosUseCase;
import com.codigo.pedido.orders.domain.port.in.ObtenerPedidoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Operaciones para crear y gestionar pedidos")
public class OrdersController {

    private static final Logger log = LoggerFactory.getLogger(OrdersController.class);

    private final CrearPedidoUseCase crearPedido;
    private final ObtenerPedidoUseCase obtenerPedido;
    private final ListarPedidosUseCase listarPedidos;
    private final ActualizarEstadoPedidoUseCase actualizarEstadoPedido;
    private final EliminarPedidoUseCase eliminarPedido;

    public OrdersController(CrearPedidoUseCase crearPedido,
                            ObtenerPedidoUseCase obtenerPedido,
                            ListarPedidosUseCase listarPedidos,
                            ActualizarEstadoPedidoUseCase actualizarEstadoPedido,
                            EliminarPedidoUseCase eliminarPedido) {
        this.crearPedido = crearPedido;
        this.obtenerPedido = obtenerPedido;
        this.listarPedidos = listarPedidos;
        this.actualizarEstadoPedido = actualizarEstadoPedido;
        this.eliminarPedido = eliminarPedido;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    @Operation(summary = "Crear pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido creado"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<PedidoResponse> crearPedido(@RequestBody CrearPedidoRequest request,
                                                      Authentication authentication) {
        UUID usuarioId = (UUID) authentication.getPrincipal();
        log.info("Request para crear pedido del usuario: {} en restaurante: {}", usuarioId, request.getRestauranteId());
        Pedido pedido = crearPedido.crear(
                usuarioId,
                request.getRestauranteId(),
                request.getItems().stream()
                        .map(item -> new CrearPedidoItemCommand(
                                item.getProductoId(),
                                item.getCantidad()
                        ))
                        .toList()
        );

        return ResponseEntity.ok(toResponse(pedido));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    @Operation(summary = "Obtener pedido por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<PedidoResponse> obtenerPedido(@PathVariable UUID id) {
        log.info("Request para obtener pedido por id: {}", id);
        return ResponseEntity.ok(toResponse(obtenerPedido.obtenerPorId(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    @Operation(summary = "Listar pedidos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public ResponseEntity<List<PedidoResponse>> listarPedidos(
            @RequestParam(required = false) UUID usuarioId,
            @RequestParam(required = false) String estado) {
        log.info("Request para listar pedidos con filtros usuarioId: {} y estado: {}", usuarioId, estado);

        List<PedidoResponse> response = listarPedidos.listar(usuarioId, estado)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    @Operation(summary = "Listar pedidos por usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public ResponseEntity<List<PedidoResponse>> listarPedidosPorUsuario(@PathVariable UUID usuarioId) {
        log.info("Request para listar pedidos del usuario: {}", usuarioId);

        List<PedidoResponse> response = listarPedidos.listar(usuarioId, null)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar estado del pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido actualizado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<PedidoResponse> actualizarEstado(
            @PathVariable UUID id,
            @RequestBody ActualizarEstadoPedidoRequest request) {
        log.info("Request para actualizar pedido {} a estado {}", id, request.getEstado());

        return ResponseEntity.ok(toResponse(actualizarEstadoPedido.actualizarEstado(id, request.getEstado())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pedido eliminado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<Void> eliminarPedido(@PathVariable UUID id) {
        log.info("Request para eliminar pedido con id: {}", id);
        eliminarPedido.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private PedidoResponse toResponse(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getUsuarioId(),
                pedido.getRestauranteId(),
                pedido.getEstado(),
                pedido.getTotal(),
                pedido.getFechaCreacion(),
                pedido.getItems().stream()
                        .map(item -> new PedidoItemResponse(
                                item.getId(),
                                item.getProductoId(),
                                item.getNombreProducto(),
                                item.getPrecio(),
                                item.getCantidad(),
                                item.getSubtotal()
                        ))
                        .toList()
        );
    }
}
