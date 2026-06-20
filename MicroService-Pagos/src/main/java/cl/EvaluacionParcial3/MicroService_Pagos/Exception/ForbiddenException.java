package cl.EvaluacionParcial3.MicroService_Pagos.Exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
