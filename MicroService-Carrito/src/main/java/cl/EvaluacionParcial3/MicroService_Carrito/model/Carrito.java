package cl.EvaluacionParcial3.MicroService_Carrito.model;

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
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_carrito")
    private Long carritoId;

    @Column(name="id_usuario")
    private Long idUsuario;

    @Column(name="fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoCarrito estado;

    @OneToMany(mappedBy="carrito",cascade=CascadeType.ALL)
    private List<ItemCarrito> items; 
}

