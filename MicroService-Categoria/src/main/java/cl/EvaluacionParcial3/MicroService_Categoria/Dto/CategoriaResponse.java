package cl.EvaluacionParcial3.MicroService_Categoria.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean estado;
    private LocalDateTime fechaCreacion;

}
