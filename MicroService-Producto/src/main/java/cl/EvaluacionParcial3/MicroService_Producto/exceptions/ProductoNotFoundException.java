package cl.EvaluacionParcial3.MicroService_Producto.exceptions;

public class ProductoNotFoundException extends RuntimeException {

    public ProductoNotFoundException(Long id) {
        super("Producto no encontrado con ID: " + id);
    }

    public ProductoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
