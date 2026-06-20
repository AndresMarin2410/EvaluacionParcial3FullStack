CREATE TABLE pagos
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    id_pedido   BIGINT      NOT NULL,
    monto DOUBLE NOT NULL,
    metodo_pago VARCHAR(30) NOT NULL,
    estado      VARCHAR(20) NOT NULL,
    fecha_pago  datetime    NOT NULL,
    CONSTRAINT pk_pagos PRIMARY KEY (id)
);