package cl.EvaluacionParcial3.MicroService_Carrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private Long itemId;
    private Long productoId;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subTotal;
}
