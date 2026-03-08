CREATE TABLE restaurantes (
    id UUID PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(20),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE productos (
    id UUID PRIMARY KEY,
    restaurante_id UUID NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio NUMERIC(10,2) NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_producto_restaurante
        FOREIGN KEY (restaurante_id)
        REFERENCES restaurantes(id)
        ON DELETE CASCADE
);