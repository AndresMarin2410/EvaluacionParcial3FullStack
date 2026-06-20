package cl.EvaluacionParcial3.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "reporte_venta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteVenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "id_reporte_venta")
    private Long id;

    @Column(name = "fecha_generacion")
    private LocalDateTime fechaGeneracion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name="fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "contenido")
    private String contenido;

    @Column(name = "tipo_reporte")
    @Enumerated(EnumType.STRING)
    private TipoReporte tipo;

    @OneToMany(mappedBy = "reporte",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleReporte> detalles;
}
