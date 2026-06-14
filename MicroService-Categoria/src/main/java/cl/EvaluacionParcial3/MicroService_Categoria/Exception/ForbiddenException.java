package cl.EvaluacionParcial3.MicroService_Categoria.Exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
