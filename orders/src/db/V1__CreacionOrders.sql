--Creacion de los pedidos y los items de los pedidos
CREATE TABLE pedidos (
                         id UUID PRIMARY KEY,
                         usuario_id UUID NOT NULL,
                         estado VARCHAR(50) NOT NULL,
                         total NUMERIC(10,2) NOT NULL,
                         fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pedido_items (
                              id UUID PRIMARY KEY,
                              pedido_id UUID NOT NULL,
                              producto_id UUID NOT NULL,
                              nombre_producto VARCHAR(150),
                              precio NUMERIC(10,2),
                              cantidad INT,
                              subtotal NUMERIC(10,2),

                              CONSTRAINT fk_pedido
                                  FOREIGN KEY (pedido_id)
                                      REFERENCES pedidos(id)
                                      ON DELETE CASCADE
);