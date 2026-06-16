CREATE TABLE inventario
(
    id                   BIGINT AUTO_INCREMENT NOT NULL,
    producto_id          BIGINT   NOT NULL,
    stock_actual         INT      NOT NULL,
    stock_min            INT      NOT NULL,
    ultima_actualizacion datetime NOT NULL,
    CONSTRAINT pk_inventario PRIMARY KEY (id)
);