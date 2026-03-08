package com.codigo.pedido.catalogo.api.controller;

import com.codigo.pedido.catalogo.api.dto.*;
import com.codigo.pedido.catalogo.domain.model.Producto;
import com.codigo.pedido.catalogo.domain.model.Restaurante;
import com.codigo.pedido.catalogo.domain.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalogo")
@Tag(name = "Catalogo", description = "Operaciones para gestionar restaurantes y productos")
public class CatalogoController {

    private static final Logger log = LoggerFactory.getLogger(CatalogoController.class);

    private final CrearRestauranteUseCase crearRestaurante;
    private final ListarRestaurantesUseCase listarRestaurantes;
    private final CrearProductoUseCase crearProducto;
    private final ListarProductosPorRestauranteUseCase listarProductos;
    private final ObtenerProductoUseCase obtenerProducto;

    public CatalogoController(
            CrearRestauranteUseCase crearRestaurante,
            ListarRestaurantesUseCase listarRestaurantes,
            CrearProductoUseCase crearProducto,
            ListarProductosPorRestauranteUseCase listarProductos,
            ObtenerProductoUseCase obtenerProducto) {

        this.crearRestaurante = crearRestaurante;
        this.listarRestaurantes = listarRestaurantes;
        this.crearProducto = crearProducto;
        this.listarProductos = listarProductos;
        this.obtenerProducto = obtenerProducto;
    }

    // ===============================
    // CREAR RESTAURANTE
    // ===============================
    @PostMapping("/restaurantes")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante creado"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<RestauranteResponse> crearRestaurante(
            @RequestBody CrearRestauranteRequest request) {
        log.info("Request para crear restaurante con nombre: {}", request.getNombre());

        Restaurante restaurante = new Restaurante(
                UUID.randomUUID(),
                request.getNombre(),
                request.getDireccion(),
                request.getTelefono()
        );

        Restaurante creado = crearRestaurante.crear(restaurante);

        RestauranteResponse response = new RestauranteResponse(
                creado.getId(),
                creado.getNombre(),
                creado.getDireccion(),
                creado.getTelefono(),
                creado.isActivo()
        );

        return ResponseEntity.ok(response);
    }

    // ===============================
    // LISTAR RESTAURANTES
    // ===============================
    @GetMapping("/restaurantes")
    @Operation(summary = "Listar restaurantes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public ResponseEntity<List<RestauranteResponse>> listarRestaurantes() {
        log.info("Request para listar restaurantes");

        List<RestauranteResponse> response = listarRestaurantes.listar()
                .stream()
                .map(r -> new RestauranteResponse(
                        r.getId(),
                        r.getNombre(),
                        r.getDireccion(),
                        r.getTelefono(),
                        r.isActivo()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ===============================
    // CREAR PRODUCTO
    // ===============================
    @PostMapping("/productos")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto creado"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<ProductoResponse> crearProducto(
            @RequestBody CrearProductoRequest request) {
        log.info("Request para crear producto con nombre: {} en restaurante: {}", request.getNombre(), request.getRestauranteId());

        Producto producto = new Producto(
                UUID.randomUUID(),
                request.getRestauranteId(),
                request.getNombre(),
                request.getDescripcion(),
                request.getPrecio()
        );

        Producto creado = crearProducto.crear(producto);

        ProductoResponse response = new ProductoResponse(
                creado.getId(),
                creado.getRestauranteId(),
                creado.getNombre(),
                creado.getDescripcion(),
                creado.getPrecio(),
                creado.isDisponible()
        );

        return ResponseEntity.ok(response);
    }

    // ===============================
    // LISTAR PRODUCTOS POR RESTAURANTE
    // ===============================
    @GetMapping("/restaurantes/{restauranteId}/productos")
    @Operation(summary = "Listar productos por restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public ResponseEntity<List<ProductoResponse>> listarProductos(
            @PathVariable UUID restauranteId) {
        log.info("Request para listar productos del restaurante: {}", restauranteId);

        List<ProductoResponse> response = listarProductos.listar(restauranteId)
                .stream()
                .map(p -> new ProductoResponse(
                        p.getId(),
                        p.getRestauranteId(),
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getPrecio(),
                        p.isDisponible()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/productos/{productoId}")
    @Operation(summary = "Obtener producto por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ProductoResponse> obtenerProducto(@PathVariable UUID productoId) {
        log.info("Request para obtener producto por id: {}", productoId);
        Producto producto = obtenerProducto.obtenerPorId(productoId);

        return ResponseEntity.ok(new ProductoResponse(
                producto.getId(),
                producto.getRestauranteId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.isDisponible()
        ));
    }
}
