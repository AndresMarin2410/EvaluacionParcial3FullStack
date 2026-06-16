package cl.EvaluacionParcial3.MicroService_Carrito.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="item_carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_item")
    private Long itemId;

    @Column(name="id_producto",nullable=false)
    private Long productoId;

    @Column(name="cantidad")
    private Integer cantidad;

    @Column(name="precio_uni")
    private Double precioUnitario;

    @ManyToOne
    @JoinColumn(name="id_carrito")
    @ToString.Exclude
    private Carrito carrito;
}

