package com.codigo.pedido.usuarios.domain.port.in;

public interface LoginUsuarioUseCase {
    String login(String correo, String contrasena);
}