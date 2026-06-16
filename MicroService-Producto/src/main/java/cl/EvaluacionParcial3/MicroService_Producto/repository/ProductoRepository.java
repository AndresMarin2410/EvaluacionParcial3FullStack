package cl.EvaluacionParcial3.MicroService_Producto.repository;

import cl.EvaluacionParcial3.MicroService_Producto.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();
    
    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    boolean existsByCategoriaId(Long categoriaId);

    List<Producto> findByNombreContainingIgnoreCase(String nombre); 

}
