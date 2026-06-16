package cl.EvaluacionParcial3.MicroService_Carrito.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
