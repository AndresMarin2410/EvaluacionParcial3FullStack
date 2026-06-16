package cl.EvaluacionParcial3.MicroService_Carrito.dto;

import lombok.Data;

@Data
public class ProductoResponse {
    private Long id;
    private String nombre;
    private Double precio;
    private Long categoriaId;
    private Boolean activo;
}
