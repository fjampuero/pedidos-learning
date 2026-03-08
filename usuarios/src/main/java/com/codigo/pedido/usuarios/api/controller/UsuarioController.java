package com.codigo.pedido.usuarios.api.controller;

import com.codigo.pedido.usuarios.api.dto.AuthResponse;
import com.codigo.pedido.usuarios.api.dto.LoginRequest;
import com.codigo.pedido.usuarios.api.dto.RegistroRequest;
import com.codigo.pedido.usuarios.api.dto.UsuarioResponse;
import com.codigo.pedido.usuarios.domain.model.Usuario;
import com.codigo.pedido.usuarios.domain.port.in.LoginUsuarioUseCase;
import com.codigo.pedido.usuarios.domain.port.in.ObtenerUsuarioUseCase;
import com.codigo.pedido.usuarios.domain.port.in.RegistrarUsuarioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones de autenticacion y consulta de usuarios")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final RegistrarUsuarioUseCase registrarUseCase;
    private final LoginUsuarioUseCase loginUseCase;
    private final ObtenerUsuarioUseCase obtenerUsuarioUseCase;

    public UsuarioController(RegistrarUsuarioUseCase registrarUseCase,
                             LoginUsuarioUseCase loginUseCase,
                             ObtenerUsuarioUseCase obtenerUsuarioUseCase) {
        this.registrarUseCase = registrarUseCase;
        this.loginUseCase = loginUseCase;
        this.obtenerUsuarioUseCase = obtenerUsuarioUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion", description = "Autentica al usuario y devuelve un JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login correcto"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "401", description = "Credenciales invalidas")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Request de login recibido para correo: {}", request.getCorreo());

        String token = loginUseCase.login(
                request.getCorreo(),
                request.getContrasena()
        );

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario y devuelve un JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario registrado"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida")
    })
    public ResponseEntity<AuthResponse> registrar(@RequestBody RegistroRequest request) {
        log.info("Request de registro recibido para correo: {}", request.getCorreo());

        String token = registrarUseCase.registrar(
                request.getNombre(),
                request.getCorreo(),
                request.getContrasena()
        );
        return ResponseEntity.ok(new AuthResponse(token));

    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UsuarioResponse> obtenerUsuario(@PathVariable UUID id) {
        log.info("Request para obtener usuario por id: {}", id);
        Usuario usuario = obtenerUsuarioUseCase.obtenerPorId(id);

        return ResponseEntity.ok(new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol(),
                usuario.isActivo()
        ));
    }
}
