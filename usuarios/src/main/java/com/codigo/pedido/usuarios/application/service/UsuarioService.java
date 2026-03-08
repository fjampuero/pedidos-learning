package com.codigo.pedido.usuarios.application.service;

import com.codigo.pedido.usuarios.domain.model.Usuario;
import com.codigo.pedido.usuarios.domain.port.in.LoginUsuarioUseCase;
import com.codigo.pedido.usuarios.domain.port.in.ObtenerUsuarioUseCase;
import com.codigo.pedido.usuarios.domain.port.in.RegistrarUsuarioUseCase;
import com.codigo.pedido.usuarios.domain.port.out.UsuarioRepositoryPort;
import com.codigo.pedido.usuarios.infrastructure.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioService implements RegistrarUsuarioUseCase, LoginUsuarioUseCase, ObtenerUsuarioUseCase {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepositoryPort usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepositoryPort usuarioRepository,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String registrar(String nombre, String correo, String contrasena) {
        log.info("Registrando usuario con correo: {}", correo);

        if (usuarioRepository.buscarPorCorreo(correo).isPresent()) {
            log.warn("Registro rechazado por correo ya existente: {}", correo);
            throw new RuntimeException("Correo ya registrado");
        }

        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                nombre,
                correo,
                passwordEncoder.encode(contrasena),
                "CLIENTE"
        );

        usuarioRepository.guardar(usuario);
        log.info("Usuario registrado correctamente con id: {}", usuario.getId());

        return jwtService.generarToken(usuario.getId(), usuario.getCorreo(), usuario.getRol());
    }

    @Override
    public String login(String correo, String contrasena) {
        log.info("Autenticando usuario con correo: {}", correo);

        Usuario usuario = usuarioRepository.buscarPorCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            log.warn("Credenciales invalidas para correo: {}", correo);
            throw new RuntimeException("Credenciales inválidas");
        }

        log.info("Autenticacion correcta para usuario id: {}", usuario.getId());

        return jwtService.generarToken(usuario.getId(), usuario.getCorreo(), usuario.getRol());
    }

    @Override
    public Usuario obtenerPorId(UUID id) {
        log.info("Consultando usuario por id: {}", id);
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
