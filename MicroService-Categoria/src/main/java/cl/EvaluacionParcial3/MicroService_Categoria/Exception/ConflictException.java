package cl.EvaluacionParcial3.MicroService_Categoria.Exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
