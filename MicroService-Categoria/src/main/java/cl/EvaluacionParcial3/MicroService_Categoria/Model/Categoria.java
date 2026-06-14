package cl.EvaluacionParcial3.MicroService_Categoria.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "categorias")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nombre_cat",unique = true,nullable = false,length = 100)
    private String nombre;

    @Column(name = "descripcion",nullable = true, length = 200)
    private String descripcion;

    @Column(name = "estado",nullable = false)
    private Boolean estado;

    @Column(name="fecha_creacion",nullable = false)
    private LocalDateTime fechaCreacion;

}
