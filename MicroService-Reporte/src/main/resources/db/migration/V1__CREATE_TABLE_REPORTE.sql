CREATE TABLE detalle_reporte
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    reporte_id  BIGINT                NULL,
    clave       VARCHAR(255)          NULL,
    valor       DOUBLE                NULL,
    descripcion VARCHAR(255)          NULL,
    CONSTRAINT pk_detalle_reporte PRIMARY KEY (id)
);

CREATE TABLE reporte_venta
(
    id_reporte_venta BIGINT AUTO_INCREMENT NOT NULL,
    fecha_generacion datetime              NULL,
    fecha_inicio     date                  NULL,
    fecha_fin        date                  NULL,
    contenido        VARCHAR(255)          NULL,
    tipo_reporte     VARCHAR(255)          NULL,
    CONSTRAINT pk_reporte_venta PRIMARY KEY (id_reporte_venta)
);

ALTER TABLE detalle_reporte
    ADD CONSTRAINT FK_DETALLE_REPORTE_ON_REPORTE FOREIGN KEY (reporte_id) REFERENCES reporte_venta (id_reporte_venta);