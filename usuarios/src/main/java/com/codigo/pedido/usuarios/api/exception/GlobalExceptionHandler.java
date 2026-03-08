package com.codigo.pedido.usuarios.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> manejarErroresValidacion(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body("Datos inválidos");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> manejarErroresGenerales(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
