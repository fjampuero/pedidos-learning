CREATE TABLE entregas (
                          id UUID PRIMARY KEY,
                          pedido_id UUID NOT NULL UNIQUE,
                          usuario_id UUID NOT NULL,
                          direccion_entrega VARCHAR(255) NOT NULL,
                          repartidor VARCHAR(100),
                          estado VARCHAR(50) NOT NULL,
                          observacion VARCHAR(255),
                          fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          fecha_entrega TIMESTAMP
);
