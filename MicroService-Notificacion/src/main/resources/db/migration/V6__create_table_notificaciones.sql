CREATE TABLE notificaciones
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    usuario_id     BIGINT      NOT NULL,
    tipo           VARCHAR(30) NOT NULL,
    mensaje        TEXT        NOT NULL,
    leida          BIT(1)      NOT NULL,
    fecha_creacion datetime    NOT NULL,
    CONSTRAINT pk_notificaciones PRIMARY KEY (id)
);