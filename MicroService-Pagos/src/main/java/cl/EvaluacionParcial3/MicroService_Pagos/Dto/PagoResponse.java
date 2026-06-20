package cl.EvaluacionParcial3.MicroService_Pagos.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoResponse {
    private Long id;
    private Long idPedido;
    private Double monto;
    private String metodoPago;
    private String estado;
    private LocalDateTime fechaPago;
}
