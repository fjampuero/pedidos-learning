CREATE TABLE pagos (
    id UUID PRIMARY KEY,
    pedido_id UUID NOT NULL UNIQUE,
    usuario_id UUID NOT NULL,
    monto NUMERIC(10,2) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    referencia_transaccion VARCHAR(100),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
