package cl.EvaluacionParcial3.MicroService_Producto.exceptions;

public class CategoriaNotFoundException extends RuntimeException {

    public CategoriaNotFoundException(Long id) {
        super("Categoría no encontrada con ID: " + id);
    }

    public CategoriaNotFoundException(String mensaje) {
        super(mensaje);
    }
}
