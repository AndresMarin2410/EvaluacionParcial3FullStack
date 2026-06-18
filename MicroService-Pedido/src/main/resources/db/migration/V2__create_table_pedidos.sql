CREATE TABLE pedidos
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    usuario_id      BIGINT       NOT NULL,
    fecha_pedido    datetime     NOT NULL,
    estado          VARCHAR(255) NOT NULL,
    total           INT          NOT NULL,
    direccion_envio VARCHAR(255) NOT NULL,
    CONSTRAINT pk_pedidos PRIMARY KEY (id)
);