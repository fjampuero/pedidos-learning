package com.codigo.pedido.usuarios.infrastructure.persistence;

import com.codigo.pedido.usuarios.domain.model.Usuario;
import com.codigo.pedido.usuarios.domain.port.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return jpaRepository.findByCorreo(correo)
                .map(this::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void guardar(Usuario usuario) {
        jpaRepository.save(toEntity(usuario));
    }

    private Usuario toDomain(UsuarioEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getNombre(),
                entity.getCorreo(),
                entity.getContrasena(),
                entity.getRol()
        );
    }

    private UsuarioEntity toEntity(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNombre(usuario.getNombre());
        entity.setCorreo(usuario.getCorreo());
        entity.setContrasena(usuario.getContrasena());
        entity.setRol(usuario.getRol());
        entity.setActivo(usuario.isActivo());
        return entity;
    }
}
