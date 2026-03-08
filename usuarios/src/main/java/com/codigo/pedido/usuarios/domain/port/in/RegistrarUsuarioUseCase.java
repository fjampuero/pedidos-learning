package com.codigo.pedido.usuarios.domain.port.in;
public interface RegistrarUsuarioUseCase {
    String registrar(String nombre, String correo, String contrasena);
}