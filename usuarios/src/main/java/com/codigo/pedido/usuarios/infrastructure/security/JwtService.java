package com.codigo.pedido.usuarios.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET = "clave_super_secreta_clave_super_secreta_12345";
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Generar token
    public String generarToken(UUID userId, String correo, String rol) {

        return Jwts.builder()
                .setSubject(correo)
                .claim("userId", userId.toString())
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extraer correo
    public String extraerCorreo(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    // Extraer rol
    public String extraerRol(String token) {
        return extraerClaim(token, claims -> claims.get("rol", String.class));
    }

    public UUID extraerUserId(String token) {
        return extraerClaim(token, claims -> UUID.fromString(claims.get("userId", String.class)));
    }

    // Extraer cualquier claim
    public <T> T extraerClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extraerTodosLosClaims(token);
        return resolver.apply(claims);
    }

    // Validar token
    public boolean esTokenValido(String token, String correo) {
        final String correoExtraido = extraerCorreo(token);
        return (correoExtraido.equals(correo) && !estaExpirado(token));
    }

    private boolean estaExpirado(String token) {
        return extraerClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
