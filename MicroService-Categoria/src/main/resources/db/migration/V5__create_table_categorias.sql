CREATE TABLE categorias
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    nombre_cat     VARCHAR(100) NOT NULL,
    descripcion    VARCHAR(200) NULL,
    estado         BIT(1)       NOT NULL,
    fecha_creacion datetime     NOT NULL,
    CONSTRAINT pk_categorias PRIMARY KEY (id)
);

ALTER TABLE categorias
    ADD CONSTRAINT uc_categorias_nombre_cat UNIQUE (nombre_cat);