package cl.EvaluacionParcial3.MicroService_Producto.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Long categoriaId;
    private Boolean activo;
    private LocalDate fechaCreacion;

}
