package cl.EvaluacionParcial3.MicroService_Carrito.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }

}
