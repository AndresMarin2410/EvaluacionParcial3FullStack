package cl.EvaluacionParcial3.MicroService_Carrito.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.EvaluacionParcial3.MicroService_Carrito.model.Carrito;
import cl.EvaluacionParcial3.MicroService_Carrito.model.EstadoCarrito;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long>{
    /**
     *  @param  CarritoId
     *  @return
      */

    // buscar el carrito actual del usuario
    // lo usamos para saber si el usuario ya tiene un carrito "ACTIVO" 
    // antes de crearle uno nuevo.
    Optional<Carrito> findByIdUsuarioAndEstado(Long idUsuario, EstadoCarrito estado);

    // buscar todos los carritos de un usuario (su historial)
    List<Carrito> findByIdUsuario(Long idUsuario);

    // buscar carritos por estado
    List<Carrito> findByEstado(EstadoCarrito estado);
}
