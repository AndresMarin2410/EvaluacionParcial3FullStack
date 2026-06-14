package cl.EvaluacionParcial3.MicroService_Categoria.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CategoriaRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre  no debe superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripcion es obligatorio")
    @Size(max = 200, message = "La descripcion no debe superar los 200 caracteres")
    private String descripcion;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "La fecha de creacion debe ser obligatoria")
    private LocalDateTime fechaCreacion;
}
