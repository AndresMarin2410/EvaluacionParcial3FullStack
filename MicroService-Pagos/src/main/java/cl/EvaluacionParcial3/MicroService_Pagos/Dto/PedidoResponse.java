package cl.EvaluacionParcial3.MicroService_Pagos.Dto;


import java.time.LocalDateTime;

public class PedidoResponse {
    private Long id;
    private Long usuarioId;
    private LocalDateTime fechaPedido;
    private String estado;
    private int total;
    private String direccionEnvio;
}
