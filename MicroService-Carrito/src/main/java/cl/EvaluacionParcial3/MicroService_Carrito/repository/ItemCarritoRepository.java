package cl.EvaluacionParcial3.MicroService_Carrito.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.EvaluacionParcial3.MicroService_Carrito.model.Carrito;
import cl.EvaluacionParcial3.MicroService_Carrito.model.ItemCarrito;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    
    //  buscar ítems por el carrito padre
    List<ItemCarrito> findByCarrito(Carrito carrito);
    
    List<ItemCarrito> findByProductoId(Long productoId);   
}
