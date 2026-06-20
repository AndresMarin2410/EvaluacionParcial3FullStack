CREATE TABLE envios
(
    id                     BIGINT AUTO_INCREMENT NOT NULL,
    pedido_id              BIGINT       NOT NULL,
    usuario_id             BIGINT       NOT NULL,
    direccion_envio        VARCHAR(255) NOT NULL,
    estado                 VARCHAR(255) NOT NULL,
    fecha_despacho         datetime     NOT NULL,
    fecha_estimada_entrega date         NOT NULL,
    empresa_envio          VARCHAR(255) NOT NULL,
    numero_seguimiento     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_envios PRIMARY KEY (id)
);