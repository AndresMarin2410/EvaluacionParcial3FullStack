package cl.EvaluacionParcial3.MicroService_Pagos.Mapper;

import cl.EvaluacionParcial3.MicroService_Pagos.Dto.PagoRequest;
import cl.EvaluacionParcial3.MicroService_Pagos.Dto.PagoResponse;
import cl.EvaluacionParcial3.MicroService_Pagos.Model.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    public Pago fromRequest(PagoRequest request){
        return Pago.builder()
                .idPedido(request.getPedidoId())
                .monto(request.getMonto())
                .metodoPago(request.getMetodoPago())
                .estado(request.getEstado())
                .fechaPago(request.getFechaPago())
                .build();
    }

    public PagoResponse toResponse(Pago pago) {
        return PagoResponse.builder()
                .id(pago.getId())
                .idPedido(pago.getIdPedido())
                .monto(pago.getMonto())
                .metodoPago(pago.getMetodoPago())
                .estado(pago.getEstado())
                .fechaPago(pago.getFechaPago())
                .build();
    }
}
